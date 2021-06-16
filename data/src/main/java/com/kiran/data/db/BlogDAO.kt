package com.kiran.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface BlogDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(blogEntity: BlogDBEntity): Long

    @Query("SELECT * FROM blogs")
    suspend fun get(): List<BlogDBEntity>
}