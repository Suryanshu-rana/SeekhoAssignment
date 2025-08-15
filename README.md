# Anime Explorer App

## Overview
Anime Explorer is a modern Android application that allows users to browse and explore top anime titles. Built with Jetpack Compose and following MVVM architecture, this app demonstrates best practices in Android development including offline caching, responsive UI, and video playback integration.

## Features
https://github.com/user-attachments/assets/0eee37e4-36e0-4afd-a056-9c6f5c3a2566
### Anime List Screen
- Grid-based display of top anime titles
- Each anime card shows:
  - Poster image
  - Title
  - Episode count
  - Rating score
- Beautiful orange and white themed UI with card shadows
- Error handling with retry functionality
- Responsive grid layout adapts to different screen sizes

### Anime Details Screen
- Comprehensive information about selected anime:
  - High-quality poster image
  - Title with rating and episode information
  - Genre list
  - Studios involved in production
  - Full synopsis
- Video trailer playback using ExoPlayer when available
- Elegant UI with section headers and organized content
- Smooth navigation with back button functionality

### Technical Features
- **Offline Support**: Caches anime data for offline viewing
- **Video Playback**: Integrated ExoPlayer for YouTube trailer playback
- **Responsive Design**: Adapts to different screen sizes and orientations
- **Custom Typography**: Implements Roboto font family throughout the app
- **Error Handling**: Graceful error states with user-friendly messages
- **Network State Management**: Handles various network conditions

## Architecture

### MVVM Pattern
The application follows the Model-View-ViewModel (MVVM) architecture pattern:
- **Model**: Data classes and repository pattern for data management
- **View**: Jetpack Compose UI components
- **ViewModel**: Manages UI-related data and business logic

### Key Components

#### Data Layer
- **Remote Data Source**: Retrofit API service for fetching anime data from Jikan API
- **Local Data Source**: Room database for offline caching
- **Repository**: Mediates between remote and local data sources

#### UI Layer
- **Screens**: Compose-based UI screens (AnimeListScreen, AnimeDetailsScreen)
- **ViewModels**: Manage UI state and business logic
- **Navigation**: Jetpack Navigation Compose for screen navigation

#### Dependency Injection
- Simple manual dependency injection through Application class

## Libraries Used

- **Jetpack Compose**: Modern UI toolkit for building native UI
- **Kotlin Coroutines & Flow**: Asynchronous programming
- **Retrofit & OkHttp**: Network requests
- **Room**: Local database storage
- **Coil**: Image loading and caching
- **ExoPlayer**: Video playback
- **Material 3**: Modern Material Design components

## Setup and Installation

1. Clone the repository
2. Open the project in Android Studio
3. Build and run the application on an emulator or physical device

## Requirements

- Android Studio Arctic Fox or newer
- Minimum SDK: Android 5.0 (API level 21)
- Target SDK: Android 13 (API level 33)
- Kotlin 1.9.0 or newer

## Future Enhancements

- Search functionality
- Favorites system
- More detailed anime information
- User reviews and ratings
- Seasonal anime listings
- Dark/Light theme toggle

## Credits

- Anime data provided by [Jikan API](https://jikan.moe/)
- Icons and design elements from Material Design

## License

This project is submitted as an assignment and is not licensed for public distribution.
