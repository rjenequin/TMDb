package com.rjenequin.tmdb.core.navigation

import kotlinx.serialization.Serializable

@Serializable
object MovieListRoute

@Serializable
data class MovieDetailRoute(val movieId: Int)