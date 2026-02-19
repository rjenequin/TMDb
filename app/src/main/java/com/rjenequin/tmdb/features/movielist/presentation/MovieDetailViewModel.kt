package com.rjenequin.tmdb.features.movielist.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.rjenequin.tmdb.core.navigation.MovieDetailRoute
import com.rjenequin.tmdb.features.movielist.domain.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieDetailViewModel @Inject constructor(
    private val repository: MovieRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val movieDetailRoute = savedStateHandle.toRoute<MovieDetailRoute>()
    val movieId = movieDetailRoute.movieId

    private val _state = MutableStateFlow(MovieDetailState())
    val state = _state.asStateFlow()

    init {
        loadMovieDetail()
    }

    private fun loadMovieDetail() {
        viewModelScope.launch {
            repository.getMovieDetails(movieId)
                .onStart { _state.update { it.copy(isLoading = true) } }
                .catch { e -> _state.update { it.copy(isLoading = false, error = e.message) } }
                .collect { detail ->
                    _state.update { it.copy(isLoading = false, movie = detail) }
                }
        }
    }
}