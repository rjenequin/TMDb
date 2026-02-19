package com.rjenequin.tmdb.core.network

import com.google.gson.annotations.SerializedName

data class MovieDetailDto(
    @SerializedName("id") val id: Int,
    @SerializedName("title") val title: String,
    @SerializedName("overview") val overview: String,
    @SerializedName("poster_path") val posterPath: String?,
    @SerializedName("release_date") val releaseDate: String?,
    @SerializedName("vote_average") val voteAverage: Double,
    @SerializedName("budget") val budget: Int,
    @SerializedName("runtime") val runtime: Int?
)