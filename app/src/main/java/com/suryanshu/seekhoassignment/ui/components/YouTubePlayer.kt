package com.suryanshu.seekhoassignment.ui.components

import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView

/**
 * A composable that displays a YouTube video player
 *
 * @param youtubeVideoId The YouTube video ID to play
 * @param modifier Modifier to be applied to the player
 * @param autoPlay Whether the video should start playing automatically
 */
@Composable
fun YouTubePlayer(
    youtubeVideoId: String,
    modifier: Modifier = Modifier,
    autoPlay: Boolean = true
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    
    // Create a YouTubePlayerView
    val youTubePlayerView = remember {
        YouTubePlayerView(context).apply {
            layoutParams = FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        }
    }
    
    // Lifecycle management for the YouTubePlayerView
    DisposableEffect(youTubePlayerView) {
        // Add the view to the lifecycle owner
        lifecycleOwner.lifecycle.addObserver(youTubePlayerView)
        
        onDispose {
            youTubePlayerView.release()
        }
    }
    
    // Render the YouTubePlayerView in Compose
    AndroidView(
        modifier = modifier,
        factory = { youTubePlayerView },
        update = { view ->
            view.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
                override fun onReady(youTubePlayer: YouTubePlayer) {
                    youTubePlayer.loadVideo(youtubeVideoId, 0f)
                    if (!autoPlay) {
                        youTubePlayer.pause()
                    }
                }
            })
        }
    )
}