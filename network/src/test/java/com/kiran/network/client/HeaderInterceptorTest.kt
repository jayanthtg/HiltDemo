package com.kiran.network.client

import com.kiran.network.client.interceptor.HeaderInterceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.Assert
import org.junit.Test

class HeaderInterceptorTest {

    @Test
    fun testHeaderInterceptor() {
        val mockWebServer = MockWebServer()
        mockWebServer.start()
        mockWebServer.enqueue(MockResponse())

        val okHttpClient = OkHttpClient().newBuilder()
            .addInterceptor(HeaderInterceptor())
            .build()
        okHttpClient.newCall(Request.Builder().url(mockWebServer.url("/")).build()).execute()

        val request = mockWebServer.takeRequest()
        Assert.assertEquals("", request.getHeader("token"))
        mockWebServer.shutdown()
    }
}