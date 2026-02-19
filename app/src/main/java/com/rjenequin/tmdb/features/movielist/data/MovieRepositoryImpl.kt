package com.rjenequin.tmdb.features.movielist.data

import com.rjenequin.tmdb.core.network.TmdbApi
import com.rjenequin.tmdb.features.movielist.domain.Movie
import com.rjenequin.tmdb.features.movielist.domain.MovieRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class MovieRepositoryImpl @Inject constructor(
    private val api: TmdbApi
) : MovieRepository {
    override fun getPopularMovies(): Flow<List<Movie>> = flow {
        val response = api.getPopularMovies()
        emit(response.results.map { it.toDomain() })
    }
}