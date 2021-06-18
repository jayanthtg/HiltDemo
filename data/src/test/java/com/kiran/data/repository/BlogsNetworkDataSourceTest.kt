package com.kiran.data.repository

import com.kiran.data.BaseTest
import com.kiran.data.network.BlogAPI
import com.kiran.data.network.BlogNetworkMapper
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert
import org.junit.Test

@ExperimentalCoroutinesApi
class BlogsNetworkDataSourceTest: BaseTest() {
    private val blogNetworkMapper = BlogNetworkMapper()
    private lateinit var blogsNetworkDataSource: BlogsNetworkDataSource
    @MockK
    lateinit var blogAPI: BlogAPI

    override fun setUp() {
        super.setUp()
        blogsNetworkDataSource = BlogsNetworkDataSourceImpl(blogAPI, blogNetworkMapper)
    }

    @Test
    fun getAPIBlogsTest() {
        coEvery { blogAPI.get() } returns getNetworkBlogs()
        runBlockingTest {
            val data = blogsNetworkDataSource.getAPIBlogs()
            Assert.assertEquals(2, data.size)
            Assert.assertEquals("Title", data[0].title)
            Assert.assertEquals("Title2", data[1].title)
        }
    }
}