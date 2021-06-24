package com.kiran.network.calladapter.coroutine

import com.kiran.network.calladapter.Retry
import com.kiran.network.calladapter.error.ExceptionKind
import com.kiran.network.calladapter.error.ServerException
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import java.time.Duration
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

const val OK = "OK"
const val PATH = "/fake"

class RetryWithErrorHandleCallAdapterFactoryTest {
    lateinit var latch: CountDownLatch

    private val a200Response by lazy { MockResponse().setResponseCode(200).setBody(OK) }
    private val a401Response by lazy { MockResponse().setResponseCode(401) }
    private val a403Response by lazy { MockResponse().setResponseCode(403) }
    private val a500Response by lazy { MockResponse().setResponseCode(500) }

    private lateinit var webServer: MockWebServer
    private lateinit var fakeService: FakeApiService

    var throwable: Throwable? = null

    @Before
    fun setUp() {
        latch = CountDownLatch(1)
        webServer = MockWebServer()
        fakeService = getFakeApiService()
    }

    @After
    fun tearDown() {
        webServer.shutdown()
    }

    @Test
    fun testCallForSocketConnection() {
        val result = java.lang.StringBuilder()
        val call = fakeService.callGet()
        call.enqueue(getFakeCallback(result))
        val ended = latch.await(1, TimeUnit.MINUTES)
        Assert.assertTrue(ended)
        Assert.assertEquals("", result.toString())
        Assert.assertTrue(throwable is ServerException)
        if(throwable is ServerException){
            Assert.assertTrue((throwable as ServerException).kind == ExceptionKind.NETWORK)
        }
        Assert.assertEquals(3, webServer.requestCount)
    }

    @Test
    fun testCallForNoConnection() {
        webServer.close()
        val result = java.lang.StringBuilder()
        val call = fakeService.callGet()
        call.enqueue(getFakeCallback(result))
        val ended = latch.await(1, TimeUnit.MINUTES)
        Assert.assertTrue(ended)
        Assert.assertEquals("", result.toString())
        Assert.assertTrue(throwable is ServerException)
        if(throwable is ServerException){
            Assert.assertTrue((throwable as ServerException).kind == ExceptionKind.NETWORK)
        }
        Assert.assertEquals(0, webServer.requestCount.toLong())
    }

    @Test
    fun testCallForAllFailure() {
        webServer.enqueue(a500Response)
        webServer.enqueue(a500Response)
        webServer.enqueue(a500Response)
        val result = java.lang.StringBuilder()
        val call = fakeService.callGet()
        call.enqueue(getFakeCallback(result))
        val ended = latch.await(1, TimeUnit.MINUTES)
        Assert.assertTrue(ended)
        Assert.assertEquals("", result.toString())
        Assert.assertTrue(throwable is ServerException)
        if(throwable is ServerException){
            val serverException = throwable as ServerException
            Assert.assertTrue(serverException.kind == ExceptionKind.HTTP)
            Assert.assertTrue(serverException.response!!.code() == 500)
        }
        Assert.assertEquals(3, webServer.requestCount)
    }

    @Test
    fun testCallForSuccessOnLastRequest() {
        webServer.enqueue(a403Response)
        webServer.enqueue(a401Response)
        webServer.enqueue(a200Response)
        val result = java.lang.StringBuilder()
        val call = fakeService.callGet()
        call.enqueue(getFakeCallback(result))
        val ended = latch.await(1, TimeUnit.MINUTES)
        Assert.assertTrue(ended)
        Assert.assertEquals(OK, result.toString())
        Assert.assertEquals(3, webServer.requestCount)
    }

    @Test
    fun testCallForMaxRetries() {
        webServer.enqueue(a500Response)
        webServer.enqueue(a500Response)
        webServer.enqueue(a500Response)
        webServer.enqueue(a500Response)
        webServer.enqueue(a500Response)
        webServer.enqueue(a500Response)
        val result = java.lang.StringBuilder()
        val call = fakeService.callRetry5Get()
        call.enqueue(getFakeCallback(result))
        val ended = latch.await(1, TimeUnit.MINUTES)
        Assert.assertTrue(ended)
        Assert.assertEquals("", result.toString())
        if(throwable is ServerException){
            val serverException = throwable as ServerException
            Assert.assertTrue(serverException.kind == ExceptionKind.HTTP)
            Assert.assertTrue(serverException.response!!.code() == 500)
        }
        Assert.assertEquals(6, webServer.requestCount)
    }

    @Test
    fun testCallForSuccess() {
        webServer.enqueue(a200Response)
        val result = java.lang.StringBuilder()
        val call = fakeService.callGet()
        call.enqueue(getFakeCallback(result))
        val ended = latch.await(1, TimeUnit.MINUTES)
        Assert.assertTrue(ended)
        Assert.assertEquals(OK, result.toString())
        Assert.assertEquals(1, webServer.requestCount)
    }

    private fun getFakeApiService() =
        getRetrofitInstance().create(FakeApiService::class.java)

    private fun getRetrofitInstance(): Retrofit {
        val seconds: Duration = Duration.ofSeconds(2)
        val client = OkHttpClient()
            .newBuilder()
            .readTimeout(seconds)
            .connectTimeout(seconds)
            .writeTimeout(seconds)
            .build()
        return Retrofit.Builder()
            .baseUrl(webServer.url("/"))
            .client(client)
            .addCallAdapterFactory(RetryWithErrorHandleCallAdapterFactory())
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()
    }

    private fun getFakeCallback(result: StringBuilder): Callback<String> {
        return object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                if (response.isSuccessful) {
                    result.append(response.body())
                }
                latch.countDown()
            }
            override fun onFailure(call: Call<String>, t: Throwable) {
                throwable = t
                latch.countDown()
            }
        }
    }

    interface FakeApiService {
        @Retry
        @GET(PATH)
        fun callGet(): Call<String>

        @Retry(5)
        @GET(PATH)
        fun callRetry5Get(): Call<String>
    }
}