package com.rjenequin.tmdb

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.rjenequin.tmdb.features.movielist.presentation.MovieListScreen
import com.rjenequin.tmdb.ui.theme.TMDbTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint // Essential for Hilt to be able to inject in an Activity
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TMDbTheme {
                // hiltViewModel() will automatically fetch the ViewModel and inject the Repo
                MovieListScreen(viewModel = hiltViewModel())
            }
        }
    }
}