package com.rjenequin.tmdb.core.di

import android.content.Context
import androidx.room.Room
import com.rjenequin.tmdb.core.database.AppDatabase
import com.rjenequin.tmdb.features.movielist.data.local.MovieDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "tmdb_database"
        ).build()
    }

    @Provides
    fun provideMovieDao(db: AppDatabase): MovieDao = db.movieDao()
}