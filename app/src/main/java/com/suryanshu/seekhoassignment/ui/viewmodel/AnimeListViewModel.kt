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

class AnimeListViewModel(application: Application) : AndroidViewModel(application) {
    
    private val repository = (application as AnimeApplication).repository
    
    private val _animeListState = MutableStateFlow<NetworkResult<List<AnimeData>>>(NetworkResult.Loading)
    val animeListState: StateFlow<NetworkResult<List<AnimeData>>> = _animeListState.asStateFlow()
    
    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing.asStateFlow()
    
    init {
        loadAnimeList()
    }
    
    fun loadAnimeList() {
        viewModelScope.launch {
            _animeListState.value = NetworkResult.Loading
            
            try {
                // Check if we have cached data first
                if (repository.hasCachedData()) {
                    // Load from database while fetching from API
                    repository.getAllAnimeFromDb().collectLatest { cachedList ->
                        if (cachedList.isNotEmpty()) {
                            _animeListState.value = NetworkResult.Success(cachedList)
                        }
                    }
                }
                
                // Fetch fresh data from API
                val result = repository.getTopAnime()
                _animeListState.value = result
            } catch (e: Exception) {
                _animeListState.value = NetworkResult.Error("Failed to load anime: ${e.message}")
            }
        }
    }
    
    fun refreshAnimeList() {
        viewModelScope.launch {
            _isRefreshing.value = true
            loadAnimeList()
            _isRefreshing.value = false
        }
    }
}