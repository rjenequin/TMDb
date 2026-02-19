package com.rjenequin.tmdb.features.movielist.presentation

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.rjenequin.tmdb.features.movielist.domain.Movie

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun MovieItem(
    movie: Movie,
    onClick: () -> Unit,
    sharedTransitionScope: SharedTransitionScope, // AJOUTÉ
    animatedVisibilityScope: AnimatedVisibilityScope, // AJOUTÉ
    modifier: Modifier = Modifier
) {
    // On utilise 'with' pour accéder aux fonctions d'extension de SharedTransitionScope
    with(sharedTransitionScope) {
        Card(
            modifier = modifier
                .fillMaxWidth()
                .clickable { onClick() },
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Row(modifier = Modifier.height(150.dp)) {
                AsyncImage(
                    model = movie.posterUrl,
                    contentDescription = "Affiche de ${movie.title}",
                    modifier = Modifier
                        .width(100.dp)
                        .fillMaxHeight()
                        // CORRECTION ICI : on change 'state' par 'sharedContentState' 👇
                        .sharedElement(
                            sharedContentState = rememberSharedContentState(key = "poster-${movie.id}"),
                            animatedVisibilityScope = animatedVisibilityScope
                        ),
                    contentScale = ContentScale.Crop
                )

                Column(
                    modifier = Modifier
                        .padding(12.dp)
                        .fillMaxSize()
                ) {
                    Text(
                        text = movie.title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = movie.releaseDate,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.secondary
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = movie.overview,
                        style = MaterialTheme.typography.bodyMedium,
                        maxLines = 3,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}