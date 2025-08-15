package com.suryanshu.seekhoassignment

import android.app.Application
import com.suryanshu.seekhoassignment.data.local.AnimeDatabase
import com.suryanshu.seekhoassignment.data.repository.AnimeRepository
import com.suryanshu.seekhoassignment.di.NetworkModule

class AnimeApplication : Application() {
    
    // Lazy initialization of the database
    private val database by lazy { AnimeDatabase.getDatabase(this) }
    
    // Repository with database and API service dependencies
    val repository by lazy { 
        AnimeRepository(
            animeDao = database.animeDao(),
            animeApiService = NetworkModule.animeApiService
        )
    }
}