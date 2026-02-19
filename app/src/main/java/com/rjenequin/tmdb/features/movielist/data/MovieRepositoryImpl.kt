package com.rjenequin.tmdb.features.movielist.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.rjenequin.tmdb.core.database.AppDatabase
import com.rjenequin.tmdb.core.network.TmdbApi
import com.rjenequin.tmdb.features.movielist.domain.Movie
import com.rjenequin.tmdb.features.movielist.domain.MovieDetail
import com.rjenequin.tmdb.features.movielist.domain.MovieRepository
import kotlinx.coroutines.flow.Flow

import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class MovieRepositoryImpl @Inject constructor(
    private val api: TmdbApi,
    private val db: AppDatabase
) : MovieRepository {
    @OptIn(ExperimentalPagingApi::class)
    override fun getMovies(query: String?): Flow<PagingData<Movie>> {
        val pagingSourceFactory = {
            if (query.isNullOrBlank()) db.movieDao().getPopularMovies()
            else db.movieDao().searchMovies(query)
        }

        return Pager(
            config = PagingConfig(pageSize = 20),
            remoteMediator = MovieRemoteMediator(api, db),
            pagingSourceFactory = pagingSourceFactory
        ).flow.map { pagingData ->
            pagingData.map { entity -> entity.toDomain() }
        }
    }

    override fun getMovieDetails(movieId: Int): Flow<MovieDetail> = flow {
        try {
            val response = api.getMovieDetails(movieId)
            emit(response.toDomain())
        } catch (e: Exception) {
            val cache = db.movieDao().getMovieById(movieId)
            if (cache != null) {
                emit(
                    MovieDetail(
                        id = cache.id,
                        title = cache.title,
                        overview = cache.overview,
                        posterUrl = "https://image.tmdb.org/t/p/w500${cache.posterPath}",
                        releaseDate = cache.releaseDate,
                        rating = cache.rating,
                        budget = "Indisponible hors-ligne"
                    )
                )
            } else {
                throw e
            }
        }
    }
}
