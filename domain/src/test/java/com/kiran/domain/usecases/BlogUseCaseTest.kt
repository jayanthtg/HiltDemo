package com.kiran.domain.usecases

import com.kiran.domain.BaseTest
import com.kiran.domain.repository.BlogRepository
import com.kiran.domain.util.ObservableData
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert
import org.junit.Test
import java.io.IOException

@ExperimentalCoroutinesApi
class BlogUseCaseTest: BaseTest() {

    private lateinit var blogUseCase: BlogUseCase

    @MockK
    lateinit var blogRepository: BlogRepository

    override fun setUp() {
        super.setUp()
        blogUseCase = BlogUseCase(blogRepository)
    }

    @Test
    fun getBlogsAPIErrorTest() {
        val ioException = IOException()
        coEvery { blogRepository.getBlogsFromAPI() }.throws(ioException)
        runBlockingTest {
            val flow = blogUseCase.getBlogs().toList()
            Assert.assertEquals(ObservableData.Loading, flow.first())
            Assert.assertEquals(ObservableData.Error(ioException), flow.last())
        }
    }

    @Test
    fun getBlogsInsertDBErrorTest() {
        val ioException = IOException()
        coEvery { blogRepository.getBlogsFromAPI() } returns getBlogs()
        coEvery { blogRepository.insertBlogs(any()) }.throws(ioException)
        runBlockingTest {
            val flow = blogUseCase.getBlogs().toList()
            Assert.assertEquals(ObservableData.Loading, flow.first())
            Assert.assertEquals(ObservableData.Error(ioException), flow.last())
        }
    }

    @Test
    fun getBlogsFetchFromDBErrorTest() {
        val ioException = IOException()
        coEvery { blogRepository.getBlogsFromAPI() } returns getBlogs()
        coEvery { blogRepository.insertBlogs(any()) } returns Unit
        coEvery { blogRepository.getBlogsFromDB() }.throws(ioException)
        runBlockingTest {
            val flow = blogUseCase.getBlogs().toList()
            Assert.assertEquals(ObservableData.Loading, flow.first())
            Assert.assertEquals(ObservableData.Error(ioException), flow.last())
        }
    }

    @Test
    fun getBlogsTest() {
        val blogs = getBlogs()
        coEvery { blogRepository.getBlogsFromAPI() } returns getBlogs()
        coEvery { blogRepository.insertBlogs(any()) } returns Unit
        coEvery { blogRepository.getBlogsFromDB() } returns blogs
        runBlockingTest {
            val flow = blogUseCase.getBlogs().toList()
            Assert.assertEquals(ObservableData.Loading, flow.first())
            Assert.assertEquals(ObservableData.Success(blogs), flow.last())
        }
    }
}