package com.kiran.network.client.interceptor

import okhttp3.Interceptor
import okhttp3.Response

class HeaderInterceptor: Interceptor {
    /**
     * Used to add Headers for API's
     */
    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request().newBuilder()
            .addHeader("token", "")
            .build()
        return chain.proceed(original)
    }
}