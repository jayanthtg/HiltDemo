package com.kiran.network

import com.kiran.network.calladapter.coroutine.OK
import com.kiran.network.calladapter.coroutine.PATH
import com.kiran.network.calladapter.coroutine.RetryWithErrorHandleCallAdapterFactory
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.whenever
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import java.time.Duration

class RetrofitApiProviderTest {

    private lateinit var webServer: MockWebServer
    private lateinit var fakeApiService: FakeApiService

    @Before
    fun setUp() {
        webServer = MockWebServer()
        val networkKit = Mockito.mock(INetworkKit::class.java)
        whenever(networkKit.provideRetrofit(any())).thenReturn(
            getRetrofitInstance()
        )
        fakeApiService = buildAPI(networkKit, webServer.url("/").host)
    }

    @After
    fun tearDown() {
        webServer.shutdown()
    }

    @Test
    fun testBuildApi() {
        val a200Response by lazy { MockResponse().setResponseCode(200).setBody(OK) }
        webServer.enqueue(a200Response)
        val call = fakeApiService.callGet()
        val response: Response<String> = call.execute()
        Assert.assertNotNull(response)
        Assert.assertEquals(OK, response.body())
        Assert.assertEquals(1, webServer.requestCount)
    }

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

    interface FakeApiService {
        @GET(PATH)
        fun callGet(): Call<String>
    }
}