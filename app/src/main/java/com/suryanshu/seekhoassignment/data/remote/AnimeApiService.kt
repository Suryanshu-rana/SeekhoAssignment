package com.suryanshu.seekhoassignment.data.remote

import com.suryanshu.seekhoassignment.data.model.AnimeDetailsResponse
import com.suryanshu.seekhoassignment.data.model.TopAnimeResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface AnimeApiService {
    
    @GET("top/anime")
    suspend fun getTopAnime(): Response<TopAnimeResponse>
    
    @GET("anime/{id}")
    suspend fun getAnimeDetails(@Path("id") animeId: Int): Response<AnimeDetailsResponse>
}