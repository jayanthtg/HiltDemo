package com.kiran.domain.usecases

import com.kiran.domain.entities.Blog
import com.kiran.domain.repository.BlogRepository
import com.kiran.domain.util.ObservableData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class BlogUseCase (
    private val blogRepository: BlogRepository
) {
    suspend fun getBlogs(): Flow<ObservableData<List<Blog>>> = flow {
        emit(ObservableData.Loading)
        try {
            val networkBlogs = blogRepository.getBlogsFromAPI()
            blogRepository.insertBlogs(networkBlogs)
            emit(ObservableData.Success(blogRepository.getBlogsFromDB()))
        }catch (e:Exception) {
            emit(ObservableData.Error(e))
        }
    }
}