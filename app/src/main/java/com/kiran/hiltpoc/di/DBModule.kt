package com.kiran.hiltpoc.di

import android.content.Context
import androidx.room.Room
import com.kiran.data.db.BlogDAO
import com.kiran.data.db.BlogDB
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityScoped
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object DBModule {

    @Singleton
    @Provides
    fun provideBlogDb(@ApplicationContext context: Context): BlogDB {
        return Room
            .databaseBuilder(
                context,
                BlogDB::class.java,
                BlogDB.DATABASE_NAME)
            .fallbackToDestructiveMigration()
            .build()
    }

    @Singleton
    @Provides
    fun provideBlogDAO(blogDB: BlogDB): BlogDAO {
        return blogDB.BlogDAO()
    }
}