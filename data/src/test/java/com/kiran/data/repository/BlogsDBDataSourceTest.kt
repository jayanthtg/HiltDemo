package com.kiran.data.repository

import com.kiran.data.BaseTest
import com.kiran.data.db.BlogDAO
import com.kiran.data.db.BlogDBMapper
import com.kiran.domain.entities.Blog
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertEquals
import org.junit.Test

@ExperimentalCoroutinesApi
class BlogsDBDataSourceTest: BaseTest() {
    private val blogDBMapper: BlogDBMapper = BlogDBMapper()
    private lateinit var blogsDBDataSource: BlogsDBDataSource

    @MockK
    private lateinit var blogDAO: BlogDAO

    override fun setUp() {
        super.setUp()
        blogsDBDataSource = BlogsDBDataSourceImpl(blogDAO, blogDBMapper)
    }

    @Test
    fun insertBlogsNoDataTest() {
        val blogs = listOf<Blog>()
        runBlockingTest {
            blogsDBDataSource.insertBlogs(blogs)
        }
        coVerify(exactly = 0) { blogDAO.insert(any()) }
    }

    @Test
    fun insertBlogsTest() {
        coEvery { blogDAO.insert(any()) } returns 0L
        runBlockingTest {
            blogsDBDataSource.insertBlogs(getBlogs())
        }
        coVerify(exactly = 2) { blogDAO.insert(any()) }
    }


    @Test
    fun getBlogsFromCacheTest() {
        coEvery { blogDAO.get() } returns getBlogDBEntity()
        runBlockingTest {
            val data = blogsDBDataSource.getBlogsFromCache()
            assertEquals(2, data.size)
            assertEquals("Title", data[0].title)
            assertEquals("Title2", data[1].title)
        }
    }
}