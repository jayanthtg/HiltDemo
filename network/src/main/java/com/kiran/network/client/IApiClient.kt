package com.kiran.network.client

import okhttp3.OkHttpClient

interface IApiClient {
    fun getOkHttpClient(): OkHttpClient
}