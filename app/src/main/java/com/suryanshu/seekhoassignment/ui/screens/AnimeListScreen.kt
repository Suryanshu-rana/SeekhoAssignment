package com.suryanshu.seekhoassignment.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.suryanshu.seekhoassignment.data.model.AnimeData
import com.suryanshu.seekhoassignment.ui.viewmodel.AnimeListViewModel
import com.suryanshu.seekhoassignment.util.NetworkResult

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnimeListScreen(
    viewModel: AnimeListViewModel = viewModel(),
    onAnimeClick: (Int) -> Unit
) {
    val animeListState by viewModel.animeListState.collectAsState()
    val isRefreshing by viewModel.isRefreshing.collectAsState()
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Anime List") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { paddingValues ->
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
        ) {
            when (animeListState) {
                is NetworkResult.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                
                is NetworkResult.Success -> {
                    val animeList = (animeListState as NetworkResult.Success<List<AnimeData>>).data
                    AnimeGrid(animeList = animeList, onAnimeClick = onAnimeClick)
                }
                
                is NetworkResult.Error -> {
                    val errorMessage = (animeListState as NetworkResult.Error).message
                    ErrorView(errorMessage = errorMessage, onRetry = { viewModel.refreshAnimeList() })
                }
            }
            
            // Pull to refresh indicator
            if (isRefreshing) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.TopCenter))
            }
        }
    }
}

@Composable
fun AnimeGrid(animeList: List<AnimeData>, onAnimeClick: (Int) -> Unit) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(8.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        items(animeList) { anime ->
            AnimeCard(anime = anime, onAnimeClick = onAnimeClick)
        }
    }
}

@Composable
fun AnimeCard(anime: AnimeData, onAnimeClick: (Int) -> Unit) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .clickable { onAnimeClick(anime.malId) },
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primary // Orange background
        )
    ) {
        Column {
            // Poster Image
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(anime.images?.jpg?.imageUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = anime.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            )
            
            // Anime Info
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = anime.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    color = Color.White // White text
                )
                
                Spacer(modifier = Modifier.height(4.dp))
                
                Text(
                    text = "Episodes: ${anime.episodes ?: "Unknown"}" ,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White // White text
                )
                
                Spacer(modifier = Modifier.height(4.dp))
                
                Text(
                    text = "Rating: ${anime.score ?: "N/A"}" ,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White // White text
                )
            }
        }
    }
}

@Composable
fun ErrorView(errorMessage: String, onRetry: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = errorMessage,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.error
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Button(onClick = onRetry) {
            Text("Retry")
        }
    }
}