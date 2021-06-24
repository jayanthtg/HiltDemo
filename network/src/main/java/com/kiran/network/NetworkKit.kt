package com.kiran.network

import com.kiran.network.client.IApiClient
import retrofit2.CallAdapter
import retrofit2.Converter
import retrofit2.Retrofit

class NetworkKit (
    private val apiClient: IApiClient,
    private val factory: Converter.Factory,
    private val callAdapter: CallAdapter.Factory? = null
): INetworkKit {

    override fun provideRetrofit(hostName: String): Retrofit =
        Retrofit.Builder().run {
            client(apiClient.getOkHttpClient())
            addConverterFactory(factory)
            baseUrl(hostName)
            callAdapter?.let {
                addCallAdapterFactory(it)
            }
            build()
        }
}