package com.rjenequin.tmdb.features.movielist.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.rjenequin.tmdb.features.movielist.domain.MovieDetail

/**
 * STATEFUL VERSION: Manages logic and the ViewModel.
 * This is the one used by Navigation.
 */
@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun MovieDetailScreen(
    viewModel: MovieDetailViewModel,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    onBackClick: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    MovieDetailContent(
        state = state,
        sharedTransitionScope = sharedTransitionScope,
        animatedVisibilityScope = animatedVisibilityScope,
        onBackClick = onBackClick
    )
}

/**
 * STATELESS VERSION: Purely for UI.
 * Ideal for Previews and tests.
 */
@OptIn(ExperimentalMaterial3Api::class, ExperimentalSharedTransitionApi::class)
@Composable
fun MovieDetailContent(
    state: MovieDetailState,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    onBackClick: () -> Unit
) {
    with(sharedTransitionScope) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Details") },
                    navigationIcon = {
                        IconButton(onClick = onBackClick) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                        }
                    }
                )
            }
        ) { padding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                if (state.isLoading) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }

                state.movie?.let { movie ->
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                    ) {
                        AsyncImage(
                            model = movie.posterUrl,
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(450.dp)
                                .sharedElement(
                                    rememberSharedContentState(key = "poster-${movie.id}"),
                                    animatedVisibilityScope = animatedVisibilityScope
                                ),
                            contentScale = ContentScale.Crop
                        )

                        Column(modifier = Modifier.padding(20.dp)) {
                            Text(
                                text = movie.title,
                                style = MaterialTheme.typography.headlineMedium,
                                fontWeight = FontWeight.Bold
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(24.dp)
                            ) {
                                InfoTag(label = "Rating", value = "⭐ ${"%.1f".format(movie.rating).toDouble()}/10")
                                InfoTag(label = "Budget", value = "💰 ${movie.budget}")
                            }

                            Spacer(modifier = Modifier.height(24.dp))

                            Text(
                                text = "Overview",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.SemiBold
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = movie.overview,
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }

                state.error?.let { error ->
                    Text(
                        text = error,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
        }
    }
}

@Composable
fun InfoTag(label: String, value: String) {
    Column {
        Text(text = label, style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.secondary)
        Text(text = value, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Medium)
    }
}

/**
 * THE PREVIEW: Short, clean, and efficient.
 */
@OptIn(ExperimentalSharedTransitionApi::class)
@Preview(showBackground = true)
@Composable
fun MovieDetailPreview() {
    val mockMovie = MovieDetail(
        id = 1,
        title = "Inception",
        overview = "A skilled thief is tasked with planting an idea into a CEO's mind.",
        posterUrl = "", // Coil will display a blank space, which is fine for the preview
        releaseDate = "2010",
        rating = 8.8,
        budget = "160M $"
    )

    SharedTransitionLayout {
        AnimatedVisibility(visible = true) {
            MovieDetailContent(
                state = MovieDetailState(movie = mockMovie),
                sharedTransitionScope = this@SharedTransitionLayout,
                animatedVisibilityScope = this@AnimatedVisibility,
                onBackClick = {}
            )
        }
    }
}
