package com.kiran.data.repository

import com.kiran.domain.entities.Blog
import com.kiran.domain.repository.BlogRepository
import com.kiran.domain.util.ObservableData

class BlogRepositoryImpl(
    private val blogsNetworkDataSource: BlogsNetworkDataSource,
    private val blogsDBDataSource: BlogsDBDataSource
) : BlogRepository {

    override suspend fun getBlogsFromAPI(): List<Blog> = blogsNetworkDataSource.getAPIBlogs()

    override suspend fun getBlogsFromDB(): List<Blog> = blogsDBDataSource.getBlogsFromCache()

    override suspend fun insertBlogs(blogs: List<Blog>) {
        blogsDBDataSource.insertBlogs(blogs)
    }
}