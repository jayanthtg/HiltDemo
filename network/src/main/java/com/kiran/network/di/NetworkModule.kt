package com.kiran.network.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.kiran.network.INetworkKit
import com.kiran.network.NetworkKit
import com.kiran.network.calladapter.coroutine.RetryWithErrorHandleCallAdapterFactory
import com.kiran.network.client.ApiClient
import com.kiran.network.client.IApiClient
import com.kiran.network.client.interceptor.HeaderInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.CallAdapter
import retrofit2.Converter
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.jaxb.JaxbConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Singleton
    @Provides
    fun providesApiClient(): IApiClient {
        return ApiClient.Builder().run {
            interceptor(HeaderInterceptor())
            build()
        }
    }

    @Singleton
    @Provides
    fun provideGsonBuilder(): Gson {
        return GsonBuilder()
            .excludeFieldsWithoutExposeAnnotation()
            .create()
    }

    @Singleton
    @Provides
    fun providesCallAdapterFactory(): CallAdapter.Factory =
        RetryWithErrorHandleCallAdapterFactory()

    @Singleton
    @Provides
    @CoroutineJsonNetworkProvider
    fun providesConvertorFactory(gson: Gson): Converter.Factory = GsonConverterFactory.create(gson)

    @Singleton
    @Provides
    @CoroutineXMLNetworkProvider
    fun providesXMLConvertorFactory(): Converter.Factory = JaxbConverterFactory.create()

    @Singleton
    @Provides
    fun providesJSONNetworkKit(
        client: IApiClient,
        @CoroutineJsonNetworkProvider gsonFactory: Converter.Factory,
        @CoroutineXMLNetworkProvider xmlFactory: Converter.Factory,
        callAdapterFactory: CallAdapter.Factory
    ): INetworkKit {
        return NetworkKit(client, gsonFactory,xmlFactory, callAdapterFactory)
    }
}