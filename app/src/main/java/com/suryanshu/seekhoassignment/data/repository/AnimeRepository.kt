package com.suryanshu.seekhoassignment.data.repository

import android.util.Log
import com.suryanshu.seekhoassignment.data.local.AnimeDao
import com.suryanshu.seekhoassignment.data.model.AnimeData
import com.suryanshu.seekhoassignment.data.model.AnimeEntity
import com.suryanshu.seekhoassignment.data.model.toAnimeData
import com.suryanshu.seekhoassignment.data.model.toEntity
import com.suryanshu.seekhoassignment.data.remote.AnimeApiService
import com.suryanshu.seekhoassignment.util.NetworkResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.io.IOException

class AnimeRepository(private val animeDao: AnimeDao, private val animeApiService: AnimeApiService) {
    
    // Get top anime list with network and database handling
    suspend fun getTopAnime(): NetworkResult<List<AnimeData>> {
        return try {
            // Try to fetch from API first
            val response = animeApiService.getTopAnime()
            if (response.isSuccessful && response.body() != null) {
                val animeList = response.body()!!.data
                // Save to database
                animeDao.insertAllAnime(animeList.map { it.toEntity() })
                NetworkResult.Success(animeList)
            } else {
                // If API call fails, try to get from database
                val localData = animeDao.getAllAnime().map { entities ->
                    entities.map { it.toAnimeData() }
                }
                if (localData.toString().isNotEmpty()) {
                    NetworkResult.Error("Unable to fetch from API, showing cached data")
                } else {
                    NetworkResult.Error("Something went wrong: ${response.message()}")
                }
            }
        } catch (e: IOException) {
            // Network error, try to get from database
            Log.e("AnimeRepository", "Network error: ${e.message}")
            NetworkResult.Error("Network error: ${e.message}")
        } catch (e: Exception) {
            Log.e("AnimeRepository", "Error fetching anime: ${e.message}")
            NetworkResult.Error("Error fetching anime: ${e.message}")
        }
    }
    
    // Get anime details by ID
    suspend fun getAnimeDetails(animeId: Int): NetworkResult<AnimeData> {
        return try {
            // Try to fetch from API first
            val response = animeApiService.getAnimeDetails(animeId)
            if (response.isSuccessful && response.body() != null && response.body()!!.data != null) {
                val animeData = response.body()!!.data!!
                // Save to database
                animeDao.insertAnime(animeData.toEntity())
                NetworkResult.Success(animeData)
            } else {
                // If API call fails, try to get from database
                NetworkResult.Error("Unable to fetch from API, trying local cache")
            }
        } catch (e: IOException) {
            // Network error, try to get from database
            Log.e("AnimeRepository", "Network error: ${e.message}")
            NetworkResult.Error("Network error: ${e.message}")
        } catch (e: Exception) {
            Log.e("AnimeRepository", "Error fetching anime details: ${e.message}")
            NetworkResult.Error("Error fetching anime details: ${e.message}")
        }
    }
    
    // Get all anime from local database
    fun getAllAnimeFromDb(): Flow<List<AnimeData>> {
        return animeDao.getAllAnime().map { entities ->
            entities.map { it.toAnimeData() }
        }
    }
    
    // Get anime by ID from local database
    fun getAnimeByIdFromDb(animeId: Int): Flow<AnimeData?> {
        return animeDao.getAnimeById(animeId).map { entity ->
            entity?.toAnimeData()
        }
    }
    
    // Check if database has data
    suspend fun hasCachedData(): Boolean {
        return animeDao.getAnimeCount() > 0
    }
}