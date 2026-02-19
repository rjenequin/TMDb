package com.rjenequin.tmdb.features.movielist.presentation

import com.rjenequin.tmdb.features.movielist.domain.Movie

data class MovieListState(
    val isLoading: Boolean = false,
    val movies: List<Movie> = emptyList(),
    val error: String? = null
)