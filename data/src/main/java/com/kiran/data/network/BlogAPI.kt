package com.kiran.data.network

import com.kiran.network.calladapter.Retry
import retrofit2.http.GET

interface BlogAPI {
    @Retry
    @GET("blogs")
    suspend fun get(): List<BlogNetworkEntity>
}