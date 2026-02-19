package com.rjenequin.tmdb.features.movielist.domain

data class MovieDetail(
    val id: Int,
    val title: String,
    val overview: String,
    val posterUrl: String,
    val releaseDate: String,
    val rating: Double,
    val budget: String
)