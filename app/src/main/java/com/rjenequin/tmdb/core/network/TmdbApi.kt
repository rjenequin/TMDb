package com.rjenequin.tmdb.core.network

import retrofit2.http.GET
import retrofit2.http.Query

interface TmdbApi {
    @GET("movie/popular")
    suspend fun getPopularMovies(
        @Query("language") language: String = "fr-FR",
        @Query("page") page: Int = 1
    ): MovieResponseDto
}