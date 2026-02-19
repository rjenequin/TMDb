package com.rjenequin.tmdb.features.movielist.domain

import kotlinx.coroutines.flow.Flow

interface MovieRepository {
    fun getPopularMovies(): Flow<List<Movie>>
}