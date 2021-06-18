package com.kiran.domain

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
    }
}