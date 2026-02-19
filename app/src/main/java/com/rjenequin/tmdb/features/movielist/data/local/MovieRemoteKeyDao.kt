package com.rjenequin.tmdb.features.movielist.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface MovieRemoteKeyDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(remoteKey: List<MovieRemoteKeys>)

    @Query("SELECT * FROM remote_keys WHERE movieId = :movieId")
    suspend fun remoteKeysMovieId(movieId: Int): MovieRemoteKeys?

    // ICI : Assure-toi que le nom correspond bien à clearRemoteKeys() 🎯
    @Query("DELETE FROM remote_keys")
    suspend fun clearRemoteKeys()
}