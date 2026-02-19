package com.rjenequin.tmdb.features.movielist.data.local

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface MovieDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(movies: List<MovieEntity>)

    @Query("SELECT * FROM movies WHERE isSearch = 0 ORDER BY page ASC")
    fun getPopularMovies(): PagingSource<Int, MovieEntity>

    @Query("SELECT * FROM movies WHERE title LIKE '%' || :query || '%' AND isSearch = 1")
    fun searchMovies(query: String): PagingSource<Int, MovieEntity>

    @Query("DELETE FROM movies WHERE isSearch = :isSearch")
    suspend fun clearAll(isSearch: Boolean)

    @Query("SELECT * FROM movies WHERE id = :movieId")
    suspend fun getMovieById(movieId: Int): MovieEntity?
}