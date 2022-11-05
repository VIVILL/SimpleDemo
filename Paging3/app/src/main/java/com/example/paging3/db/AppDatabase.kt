package com.example.paging3.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.paging3.dao.ArticleDao
import com.example.paging3.data.Article
import com.example.paging3.data.DateConverter


@Database(entities = [Article::class], version = 1)
@TypeConverters(DateConverter::class)
abstract class AppDatabase : RoomDatabase(){
    abstract fun articleDao(): ArticleDao

    companion object{

        @Volatile
        private var INSTANCE: AppDatabase? = null

        @JvmStatic
        fun getDatabase(applicationContext: Context): AppDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            //锁
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    applicationContext,
                    AppDatabase::class.java, "article.db"
                )
                    .build()
                INSTANCE = instance
                return instance
            }

        }
    }
}