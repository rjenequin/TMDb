package com.rjenequin.tmdb.features.movielist.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.rjenequin.tmdb.core.network.TmdbApi
import com.rjenequin.tmdb.features.movielist.domain.Movie

class MoviePagingSource(
    private val api: TmdbApi,
    private val query: String? = null // If null, load popular movies
) : PagingSource<Int, Movie>() {

    override fun getRefreshKey(state: PagingState<Int, Movie>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {
        val position = params.key ?: 1 // Starting page = 1
        return try {
            val response = if (query.isNullOrBlank()) {
                api.getPopularMovies(page = position)
            } else {
                api.searchMovies(query = query, page = position)
            }

            val movies = response.results.map { it.toDomain() }

            LoadResult.Page(
                data = movies,
                prevKey = if (position == 1) null else position - 1,
                nextKey = if (movies.isEmpty()) null else position + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}