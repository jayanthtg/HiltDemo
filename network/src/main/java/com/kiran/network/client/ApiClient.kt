package com.kiran.network.client

import com.kiran.network.*
import okhttp3.Authenticator
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit

class ApiClient private constructor(
    private val interceptors: List<Interceptor>?,
    private val authenticator: Authenticator?
): IApiClient {

    /**
     * Creating new Builder for client.
     */
    override fun getOkHttpClient(): OkHttpClient =
        getDefaultLazyClient().value
            .newBuilder().run {
                interceptors?.forEach { interceptor ->
                    addInterceptor(interceptor)
                }
                authenticator?.let { authenticator ->
                    authenticator(authenticator)
                }
                //Adding SSL Socket Factory
                //Certificate Pinner
                build()
            }

    private fun getDefaultLazyClient() = lazy {
        OkHttpClient.Builder().run {
            connectTimeout(CONNECTION_TIMEOUT_IN_SEC, TimeUnit.SECONDS)
            readTimeout(READ_TIMEOUT_IN_SEC, TimeUnit.SECONDS)
            callTimeout(CALL_TIMEOUT_IN_SEC, TimeUnit.SECONDS)
            /**
             * Note: Check this, By default both are true.
             * followRedirects(false)
             * followSslRedirects(false)
             */
            addInterceptor(
                HttpLoggingInterceptor().apply {
                    level = if (BuildConfig.DEBUG)
                        HttpLoggingInterceptor.Level.BODY else
                        HttpLoggingInterceptor.Level.NONE
                }
            )
            build()
        }
    }

    /**
     * Builder class
     */
    data class Builder(
        var interceptors: MutableList<Interceptor>? = null,
        var authenticator: Authenticator? = null
    ) {
        fun interceptors(vararg interceptors: Interceptor) = apply {
            this.interceptors = mutableListOf(*interceptors)
        }

        fun authenticator(authenticator: Authenticator) = apply {
            this.authenticator = authenticator
        }

        fun interceptor(interceptor: Interceptor) = apply {
            if (interceptors == null) {
                interceptors = mutableListOf()
            }
            interceptors!!.add(interceptor)
        }

        fun build() = ApiClient(interceptors, authenticator)
    }
}