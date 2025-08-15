package com.suryanshu.seekhoassignment.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.suryanshu.seekhoassignment.data.model.AnimeEntity
import com.suryanshu.seekhoassignment.data.model.GenreConverter
import com.suryanshu.seekhoassignment.data.model.StudioConverter

@Database(entities = [AnimeEntity::class], version = 1, exportSchema = false)
@TypeConverters(GenreConverter::class, StudioConverter::class)
abstract class AnimeDatabase : RoomDatabase() {
    
    abstract fun animeDao(): AnimeDao
    
    companion object {
        @Volatile
        private var INSTANCE: AnimeDatabase? = null
        
        fun getDatabase(context: Context): AnimeDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AnimeDatabase::class.java,
                    "anime_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}