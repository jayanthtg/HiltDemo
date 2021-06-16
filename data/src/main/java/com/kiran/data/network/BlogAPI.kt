package com.kiran.data.network

import retrofit2.http.GET

interface BlogAPI {
    @GET("blogs")
    suspend fun get(): List<BlogNetworkEntity>
}