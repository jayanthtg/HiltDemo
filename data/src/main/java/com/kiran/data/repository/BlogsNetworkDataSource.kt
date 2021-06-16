package com.kiran.data.repository

import com.kiran.domain.entities.Blog

interface BlogsNetworkDataSource {
    suspend fun getAPIBlogs(): List<Blog>
}