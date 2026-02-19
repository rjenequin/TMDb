package com.rjenequin.tmdb.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.rjenequin.tmdb.features.movielist.data.local.MovieDao
import com.rjenequin.tmdb.features.movielist.data.local.MovieEntity
import com.rjenequin.tmdb.features.movielist.data.local.MovieRemoteKeyDao
import com.rjenequin.tmdb.features.movielist.data.local.MovieRemoteKeys

@Database(
    entities = [MovieEntity::class, MovieRemoteKeys::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun movieDao(): MovieDao
    abstract fun movieRemoteKeyDao(): MovieRemoteKeyDao
}