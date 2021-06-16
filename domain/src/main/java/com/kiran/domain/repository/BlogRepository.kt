package com.kiran.domain.repository

import com.kiran.domain.entities.Blog

interface BlogRepository {

    suspend fun getBlogsFromAPI(): List<Blog>

    suspend fun insertBlogs(blogs: List<Blog>)

    suspend fun getBlogsFromDB(): List<Blog>
}