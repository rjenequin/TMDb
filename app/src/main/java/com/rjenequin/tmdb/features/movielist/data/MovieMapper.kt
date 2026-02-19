package com.rjenequin.tmdb.features.movielist.data

import com.rjenequin.tmdb.core.network.MovieDto
import com.rjenequin.tmdb.features.movielist.domain.Movie

fun MovieDto.toDomain(): Movie {
    return Movie(
        id = id,
        title = title,
        overview = overview,
        posterUrl = "https://image.tmdb.org/t/p/w500$posterPath",
        releaseDate = releaseDate ?: ""
    )
}