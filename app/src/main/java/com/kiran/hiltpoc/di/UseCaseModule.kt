package com.kiran.hiltpoc.di

import com.kiran.data.db.BlogDAO
import com.kiran.data.db.BlogDBMapper
import com.kiran.data.network.BlogAPI
import com.kiran.data.network.BlogNetworkMapper
import com.kiran.data.repository.*
import com.kiran.domain.repository.BlogRepository
import com.kiran.domain.usecases.BlogUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object UseCaseModule {

    @ViewModelScoped
    @Provides
    fun providesBlogsNetworkDataSource(blogAPI: BlogAPI, blogNetworkMapper: BlogNetworkMapper): BlogsNetworkDataSource =
        BlogsNetworkDataSourceImpl(blogAPI, blogNetworkMapper)

    @ViewModelScoped
    @Provides
    fun providesBlogsDBDataSource(blogDAO: BlogDAO, blogDBMapper: BlogDBMapper): BlogsDBDataSource =
        BlogsDBDataSourceImpl(blogDAO, blogDBMapper)

    @ViewModelScoped
    @Provides
    fun providesBlogRepository(blogsNetworkDataSource: BlogsNetworkDataSource, blogsDBDataSource: BlogsDBDataSource): BlogRepository =
        BlogRepositoryImpl(blogsNetworkDataSource, blogsDBDataSource)

    @ViewModelScoped
    @Provides
    fun provideDataUseCase(
        blogRepository: BlogRepository
    ): BlogUseCase{
        return BlogUseCase(blogRepository)
    }
}