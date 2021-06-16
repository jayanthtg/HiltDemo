package com.kiran.data.repository

import com.kiran.data.network.BlogAPI
import com.kiran.data.network.BlogNetworkMapper
import com.kiran.domain.entities.Blog

class BlogsNetworkDataSourceImpl(
    private val blogAPI: BlogAPI,
    private val blogNetworkMapper: BlogNetworkMapper
    ): BlogsNetworkDataSource {

    override suspend fun getAPIBlogs(): List<Blog> {
        val networkBlogs = blogAPI.get()
        return blogNetworkMapper.mapFromEntityList(networkBlogs)
    }
}