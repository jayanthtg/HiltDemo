package com.kiran.data.repository

import com.kiran.domain.entities.Blog
import com.kiran.domain.util.ObservableData
import kotlinx.coroutines.flow.Flow

interface BlogsDBDataSource {
    suspend fun insertBlogs(blogs: List<Blog>)
    suspend fun getBlogsFromCache(): List<Blog>
}