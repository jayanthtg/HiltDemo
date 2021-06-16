package com.kiran.data.repository

import com.kiran.data.db.BlogDAO
import com.kiran.data.db.BlogDBMapper
import com.kiran.domain.entities.Blog

class BlogsDBDataSourceImpl(
    private val blogDAO: BlogDAO,
    private val blogDBMapper: BlogDBMapper
): BlogsDBDataSource {

    override suspend fun insertBlogs(blogs: List<Blog>) {
        for(blog in blogs){
            blogDAO.insert(blogDBMapper.mapToEntity(blog))
        }
    }

    override suspend fun getBlogsFromCache(): List<Blog> {
        val cachedBlogs = blogDAO.get()
        return blogDBMapper.mapFromEntityList(cachedBlogs)
    }
}