package com.rjenequin.tmdb.features.movielist.presentation

import com.rjenequin.tmdb.features.movielist.domain.MovieDetail

data class MovieDetailState(
    val isLoading: Boolean = false,
    val movie: MovieDetail? = null,
    val error: String? = null
)