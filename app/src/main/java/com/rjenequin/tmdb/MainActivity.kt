package com.rjenequin.tmdb

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.rjenequin.tmdb.core.navigation.MovieDetailRoute
import com.rjenequin.tmdb.core.navigation.MovieListRoute
import com.rjenequin.tmdb.features.movielist.presentation.MovieDetailScreen
import com.rjenequin.tmdb.features.movielist.presentation.MovieListScreen
import com.rjenequin.tmdb.ui.theme.TMDbTheme
import dagger.hilt.android.AndroidEntryPoint

@OptIn(ExperimentalSharedTransitionApi::class) // API still experimental but stable in 2026
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TMDbTheme {
                val navController = rememberNavController()

                // We wrap everything in a SharedTransitionLayout
                SharedTransitionLayout {
                    NavHost(
                        navController = navController,
                        startDestination = MovieListRoute
                    ) {
                        composable<MovieListRoute> {
                            MovieListScreen(
                                viewModel = hiltViewModel(),
                                // We pass the scopes to the screen
                                sharedTransitionScope = this@SharedTransitionLayout,
                                animatedVisibilityScope = this@composable,
                                onMovieClick = { movieId ->
                                    navController.navigate(MovieDetailRoute(movieId))
                                }
                            )
                        }

                        composable<MovieDetailRoute> {
                            MovieDetailScreen(
                                viewModel = hiltViewModel(),
                                // We pass the same scopes here
                                sharedTransitionScope = this@SharedTransitionLayout,
                                animatedVisibilityScope = this@composable,
                                onBackClick = { navController.popBackStack() }
                            )
                        }
                    }
                }
            }
        }
    }
}