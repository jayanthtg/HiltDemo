package com.kiran.data

import com.kiran.data.db.BlogDBEntity
import com.kiran.data.network.BlogNetworkEntity
import com.kiran.domain.entities.Blog
import io.mockk.MockKAnnotations
import io.mockk.unmockkAll
import org.junit.After
import org.junit.Before

open class BaseTest {

    @Before
    open fun setUp() {
        MockKAnnotations.init(this)
    }

    @After
    open fun tearDown() {
        unmockkAll()
    }

    companion object {
        fun getBlogs(): List<Blog> {
            val blogs = mutableListOf<Blog>()
            blogs.add(Blog(1, "Title", "body", "image", "category"))
            blogs.add(Blog(2, "Title2", "body", "image", "category"))
            return blogs
        }

        fun getBlogDBEntity(): List<BlogDBEntity> {
            val blogDBEntities = mutableListOf<BlogDBEntity>()
            blogDBEntities.add(BlogDBEntity(1, "Title", "body", "image", "category"))
            blogDBEntities.add(BlogDBEntity(2, "Title2", "body", "image", "category"))
            return blogDBEntities
        }

        fun getNetworkBlogs(): List<BlogNetworkEntity> {
            val blogNetworkEntities = mutableListOf<BlogNetworkEntity>()
            blogNetworkEntities.add(BlogNetworkEntity(1, "Title", "body", "image", "category"))
            blogNetworkEntities.add(BlogNetworkEntity(2, "Title2", "body", "image", "category"))
            return blogNetworkEntities
        }
    }
}