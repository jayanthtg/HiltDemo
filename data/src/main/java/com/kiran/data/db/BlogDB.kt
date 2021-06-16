package com.kiran.data.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [BlogDBEntity::class], version = 1)
abstract class BlogDB: RoomDatabase() {

    abstract fun BlogDAO(): BlogDAO

    companion object{
        val DATABASE_NAME: String = "blog_db"
    }
}