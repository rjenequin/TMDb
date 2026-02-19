package com.rjenequin.tmdb.features.movielist.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.rjenequin.tmdb.core.database.AppDatabase
import com.rjenequin.tmdb.core.network.TmdbApi
import com.rjenequin.tmdb.features.movielist.data.local.MovieEntity
import com.rjenequin.tmdb.features.movielist.data.local.MovieRemoteKeys

@OptIn(ExperimentalPagingApi::class)
class MovieRemoteMediator(
    private val api: TmdbApi,
    private val db: AppDatabase
) : RemoteMediator<Int, MovieEntity>() {

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, MovieEntity>
    ): MediatorResult {
        val page = when (loadType) {
            LoadType.REFRESH -> 1
            LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
            LoadType.APPEND -> {
                val remoteKeys = getRemoteKeyForLastItem(state)
                val nextKey = remoteKeys?.nextKey ?: return MediatorResult.Success(
                    endOfPaginationReached = remoteKeys != null
                )
                nextKey
            }
        }

        try {
            val response = api.getPopularMovies(page = page)
            val isEndOfList = response.results.isEmpty()

            db.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    db.movieRemoteKeyDao().clearRemoteKeys()
                    db.movieDao().clearAll(isSearch = false)
                }
                val prevKey = if (page == 1) null else page - 1
                val nextKey = if (isEndOfList) null else page + 1
                val keys = response.results.map {
                    MovieRemoteKeys(movieId = it.id, prevKey = prevKey, nextKey = nextKey)
                }
                db.movieRemoteKeyDao().insertAll(keys)
                db.movieDao().insertAll(response.results.map { it.toEntity(page) })
            }
            return MediatorResult.Success(endOfPaginationReached = isEndOfList)
        } catch (e: Exception) {
            return MediatorResult.Error(e)
        }
    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, MovieEntity>): MovieRemoteKeys? {
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()?.let { movie ->
            db.movieRemoteKeyDao().remoteKeysMovieId(movie.id)
        }
    }
}