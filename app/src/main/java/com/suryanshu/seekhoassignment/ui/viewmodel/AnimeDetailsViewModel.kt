package com.suryanshu.seekhoassignment.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.suryanshu.seekhoassignment.AnimeApplication
import com.suryanshu.seekhoassignment.data.model.AnimeData
import com.suryanshu.seekhoassignment.util.NetworkResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class AnimeDetailsViewModel(application: Application) : AndroidViewModel(application) {
    
    private val repository = (application as AnimeApplication).repository
    
    private val _animeDetailsState = MutableStateFlow<NetworkResult<AnimeData>>(NetworkResult.Loading)
    val animeDetailsState: StateFlow<NetworkResult<AnimeData>> = _animeDetailsState.asStateFlow()
    
    fun loadAnimeDetails(animeId: Int) {
        viewModelScope.launch {
            _animeDetailsState.value = NetworkResult.Loading
            
            try {
                // Try to get from local database first
                repository.getAnimeByIdFromDb(animeId).collectLatest { cachedAnime ->
                    if (cachedAnime != null) {
                        _animeDetailsState.value = NetworkResult.Success(cachedAnime)
                    }
                }
                
                // Then fetch fresh data from API
                val result = repository.getAnimeDetails(animeId)
                _animeDetailsState.value = result
            } catch (e: Exception) {
                _animeDetailsState.value = NetworkResult.Error("Failed to load anime details: ${e.message}")
            }
        }
    }
}