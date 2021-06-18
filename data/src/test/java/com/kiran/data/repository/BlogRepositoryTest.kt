package com.kiran.data.repository

import com.kiran.data.BaseTest
import com.kiran.domain.repository.BlogRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert
import org.junit.Test

@ExperimentalCoroutinesApi
class BlogRepositoryTest: BaseTest() {

    private lateinit var blogRepository: BlogRepository

    @MockK
    lateinit var blogsNetworkDataSource: BlogsNetworkDataSource
    @MockK
    lateinit var blogsDBDataSource: BlogsDBDataSource

    override fun setUp() {
        super.setUp()
        blogRepository = BlogRepositoryImpl(blogsNetworkDataSource, blogsDBDataSource)
    }

    @Test
    fun getBlogsFromAPITest() {
        coEvery { blogsNetworkDataSource.getAPIBlogs() } returns getBlogs()
        runBlockingTest {
            val data = blogRepository.getBlogsFromAPI()
            Assert.assertEquals(2, data.size)
            Assert.assertEquals("Title", data[0].title)
            Assert.assertEquals("Title2", data[1].title)
        }
    }

    @Test
    fun insertBlogsTest() {
        coEvery { blogsDBDataSource.insertBlogs(any()) } returns Unit
        runBlockingTest {
            blogRepository.insertBlogs(getBlogs())
            coVerify { blogsDBDataSource.insertBlogs(any()) }
        }
    }

    @Test
    fun getBlogsFromDBTest() {
        coEvery { blogsDBDataSource.getBlogsFromCache() } returns getBlogs()
        runBlockingTest {
            val data = blogRepository.getBlogsFromDB()
            Assert.assertEquals(2, data.size)
            Assert.assertEquals("Title", data[0].title)
            Assert.assertEquals("Title2", data[1].title)
        }
    }
}