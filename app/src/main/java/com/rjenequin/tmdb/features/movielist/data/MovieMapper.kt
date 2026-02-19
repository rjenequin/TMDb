package com.rjenequin.tmdb.features.movielist.data

import com.rjenequin.tmdb.core.network.MovieDetailDto
import com.rjenequin.tmdb.core.network.MovieDto
import com.rjenequin.tmdb.features.movielist.data.local.MovieEntity
import com.rjenequin.tmdb.features.movielist.domain.Movie
import com.rjenequin.tmdb.features.movielist.domain.MovieDetail

fun MovieDto.toDomain(): Movie {
    return Movie(
        id = id,
        title = title,
        overview = overview,
        posterUrl = "https://image.tmdb.org/t/p/w500$posterPath",
        releaseDate = releaseDate ?: ""
    )
}

fun MovieDto.toEntity(page: Int): MovieEntity {
    return MovieEntity(
        id = id,
        title = title,
        overview = overview,
        posterPath = posterPath,
        releaseDate = releaseDate ?: "",
        rating = voteAverage,
        page = page,
        isSearch = false
    )
}

fun MovieEntity.toDomain(): Movie {
    return Movie(
        id = id,
        title = title,
        overview = overview,
        posterUrl = "https://image.tmdb.org/t/p/w500$posterPath",
        releaseDate = releaseDate
    )
}

fun MovieDetailDto.toDomain(): MovieDetail {
    return MovieDetail(
        id = id,
        title = title,
        overview = overview,
        posterUrl = "https://image.tmdb.org/t/p/w500$posterPath",
        releaseDate = releaseDate ?: "",
        rating = voteAverage,
        budget = if (budget > 0) "$budget $" else "Inconnu"
    )
}