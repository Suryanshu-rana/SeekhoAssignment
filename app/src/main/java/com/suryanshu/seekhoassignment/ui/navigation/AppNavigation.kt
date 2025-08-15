package com.suryanshu.seekhoassignment.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.suryanshu.seekhoassignment.ui.screens.AnimeDetailsScreen
import com.suryanshu.seekhoassignment.ui.screens.AnimeListScreen

object AppDestinations {
    const val ANIME_LIST = "anime_list"
    const val ANIME_DETAILS = "anime_details/{animeId}"
    
    fun animeDetailsRoute(animeId: Int): String = "anime_details/$animeId"
}

@Composable
fun AppNavigation(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = AppDestinations.ANIME_LIST
    ) {
        composable(AppDestinations.ANIME_LIST) {
            AnimeListScreen(onAnimeClick = { animeId ->
                navController.navigate(AppDestinations.animeDetailsRoute(animeId))
            })
        }
        
        composable(
            route = AppDestinations.ANIME_DETAILS,
            arguments = listOf(navArgument("animeId") { type = NavType.IntType })
        ) { backStackEntry ->
            val animeId = backStackEntry.arguments?.getInt("animeId") ?: 0
            AnimeDetailsScreen(
                animeId = animeId,
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}