package com.kiran.network

import retrofit2.Retrofit

interface INetworkKit {
    fun provideRetrofit(hostName: String): Retrofit
}