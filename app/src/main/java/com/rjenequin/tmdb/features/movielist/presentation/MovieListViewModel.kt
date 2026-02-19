package com.rjenequin.tmdb.features.movielist.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rjenequin.tmdb.features.movielist.domain.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieListViewModel @Inject constructor(
    private val repository: MovieRepository
) : ViewModel() {

    // Private MutableStateFlow to modify the state
    private val _state = MutableStateFlow(MovieListState())
    // Public StateFlow (read-only) for the Compose view
    val state: StateFlow<MovieListState> = _state.asStateFlow()

    init {
        loadMovies()
    }

    fun loadMovies() {
        viewModelScope.launch {
            repository.getPopularMovies()
                .onStart {
                    _state.value = _state.value.copy(isLoading = true, error = null)
                }
                .catch { e ->
                    _state.value = _state.value.copy(isLoading = false, error = e.message)
                }
                .collect { movieList ->
                    _state.value = _state.value.copy(isLoading = false, movies = movieList)
                }
        }
    }
}