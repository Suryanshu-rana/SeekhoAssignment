package com.suryanshu.seekhoassignment.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.suryanshu.seekhoassignment.data.model.AnimeEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AnimeDao {
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAnime(anime: AnimeEntity)
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllAnime(animeList: List<AnimeEntity>)
    
    @Query("SELECT * FROM anime ORDER BY score DESC")
    fun getAllAnime(): Flow<List<AnimeEntity>>
    
    @Query("SELECT * FROM anime WHERE malId = :animeId")
    fun getAnimeById(animeId: Int): Flow<AnimeEntity?>
    
    @Query("DELETE FROM anime")
    suspend fun deleteAllAnime()
    
    @Query("SELECT COUNT(*) FROM anime")
    suspend fun getAnimeCount(): Int
}