package com.suryanshu.seekhoassignment.util

import android.content.Context
import android.net.Uri
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.DefaultMediaSourceFactory

/**
 * Utility class for handling video playback with ExoPlayer
 */
object VideoPlayerUtil {
    
    /**
     * Creates an ExoPlayer instance configured for playing YouTube videos
     * 
     * @param context The application context
     * @param videoUrl The URL of the video to play
     * @param autoPlay Whether the video should start playing automatically
     * @return Configured ExoPlayer instance
     */
    fun createExoPlayer(context: Context, videoUrl: String, autoPlay: Boolean = true): ExoPlayer {
        return ExoPlayer.Builder(context)
            .setMediaSourceFactory(DefaultMediaSourceFactory(context))
            .build().apply {
                val mediaItem = MediaItem.fromUri(Uri.parse(videoUrl))
                setMediaItem(mediaItem)
                
                // Set player properties
                playWhenReady = autoPlay
                repeatMode = Player.REPEAT_MODE_OFF
                
                // Prepare the player
                prepare()
            }
    }
    
    /**
     * Extracts YouTube video ID from various YouTube URL formats
     * 
     * @param url The YouTube URL
     * @return The extracted video ID or null if not found
     */
    fun extractYouTubeId(url: String?): String? {
        if (url.isNullOrEmpty()) return null
        
        // Extract from standard YouTube URL
        val standardPattern = "v=([a-zA-Z0-9_-]{11})"
        val standardRegex = standardPattern.toRegex()
        val standardMatch = standardRegex.find(url)
        if (standardMatch != null) {
            return standardMatch.groupValues[1]
        }
        
        // Extract from youtu.be URL
        val shortPattern = "youtu\\.be/([a-zA-Z0-9_-]{11})"
        val shortRegex = shortPattern.toRegex()
        val shortMatch = shortRegex.find(url)
        if (shortMatch != null) {
            return shortMatch.groupValues[1]
        }
        
        // Extract from embed URL
        val embedPattern = "embed/([a-zA-Z0-9_-]{11})"
        val embedRegex = embedPattern.toRegex()
        val embedMatch = embedRegex.find(url)
        if (embedMatch != null) {
            return embedMatch.groupValues[1]
        }
        
        return null
    }
    
    /**
     * Converts a YouTube video ID to a playable URL
     * 
     * @param videoId The YouTube video ID
     * @return A playable URL for the video
     */
    fun getYouTubeUrl(videoId: String): String {
        return "https://www.youtube.com/watch?v=$videoId"
    }
}