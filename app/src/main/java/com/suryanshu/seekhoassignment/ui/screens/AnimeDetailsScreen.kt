package com.suryanshu.seekhoassignment.ui.screens

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.suryanshu.seekhoassignment.data.model.AnimeData
import com.suryanshu.seekhoassignment.ui.viewmodel.AnimeDetailsViewModel
import com.suryanshu.seekhoassignment.util.NetworkResult

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnimeDetailsScreen(
    animeId: Int,
    viewModel: AnimeDetailsViewModel = viewModel(),
    onBackClick: () -> Unit
) {
    val animeDetailsState by viewModel.animeDetailsState.collectAsState()
    
    // Load anime details when the screen is first composed
    LaunchedEffect(animeId) {
        viewModel.loadAnimeDetails(animeId)
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Anime Details") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (animeDetailsState) {
                is NetworkResult.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                
                is NetworkResult.Success -> {
                    val animeData = (animeDetailsState as NetworkResult.Success<AnimeData>).data
                    AnimeDetailsContent(animeData = animeData)
                }
                
                is NetworkResult.Error -> {
                    val errorMessage = (animeDetailsState as NetworkResult.Error).message
                    ErrorView(
                        errorMessage = errorMessage,
                        onRetry = { viewModel.loadAnimeDetails(animeId) }
                    )
                }
            }
        }
    }
}

@Composable
fun AnimeDetailsContent(animeData: AnimeData) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()
    
    // Setup ExoPlayer if trailer is available
    val hasTrailer = !animeData.trailer?.youtubeId.isNullOrEmpty() || !animeData.trailer?.url.isNullOrEmpty()
    val exoPlayer = remember {
        if (hasTrailer) {
            ExoPlayer.Builder(context).build().apply {
                val trailerUrl = animeData.trailer?.url
                if (!trailerUrl.isNullOrEmpty()) {
                    val mediaItem = MediaItem.fromUri(Uri.parse(trailerUrl))
                    setMediaItem(mediaItem)
                    prepare()
                }
            }
        } else null
    }
    
    // Clean up ExoPlayer when leaving the screen
    DisposableEffect(Unit) {
        onDispose {
            exoPlayer?.release()
        }
    }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp)
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Video Player or Poster Image
        if (hasTrailer && exoPlayer != null) {
            // Video Player
            AndroidView(
                factory = { ctx ->
                    PlayerView(ctx).apply {
                        player = exoPlayer
                        useController = true
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)
            )
        } else {
            // Poster Image
            AsyncImage(
                model = ImageRequest.Builder(context)
                    .data(animeData.images?.jpg?.largeImageUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = animeData.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)
            )
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Title
        Text(
            text = animeData.title,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary // Orange color
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        // Rating and Episodes
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Rating: ${animeData.score ?: "N/A"}",
                style = MaterialTheme.typography.bodyLarge
            )
            
            Text(
                text = "Episodes: ${animeData.episodes ?: "Unknown"}",
                style = MaterialTheme.typography.bodyLarge
            )
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Genres
        Text(
            text = "Genres",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary // Orange color
        )
        
        Spacer(modifier = Modifier.height(4.dp))
        
        Text(
            text = animeData.genres?.joinToString(", ") { it.name } ?: "Unknown",
            style = MaterialTheme.typography.bodyLarge
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Studios
        Text(
            text = "Studios",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary // Orange color
        )
        
        Spacer(modifier = Modifier.height(4.dp))
        
        Text(
            text = animeData.studios?.joinToString(", ") { it.name } ?: "Unknown",
            style = MaterialTheme.typography.bodyLarge
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Synopsis
        Text(
            text = "Synopsis",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary // Orange color
        )
        
        Spacer(modifier = Modifier.height(4.dp))
        
        Text(
            text = animeData.synopsis ?: "No synopsis available.",
            style = MaterialTheme.typography.bodyLarge
        )
    }
}