package com.kiran.hiltpoc.ui.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.scopes.ActivityScoped

@InstallIn(ActivityComponent::class)
@Module
object MainActivityModule {

    @Provides
    @ActivityScoped
    fun provideStr(): String = "Demo"
}