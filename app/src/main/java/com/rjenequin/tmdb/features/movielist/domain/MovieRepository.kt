package com.rjenequin.tmdb.features.movielist.domain

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow

interface MovieRepository {
    fun getMovies(query: String?): Flow<PagingData<Movie>>
    fun getMovieDetails(movieId: Int): Flow<MovieDetail>
}