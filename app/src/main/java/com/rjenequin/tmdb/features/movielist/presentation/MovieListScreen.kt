package com.rjenequin.tmdb.features.movielist.presentation

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey

@OptIn(ExperimentalMaterial3Api::class, ExperimentalSharedTransitionApi::class)
@Composable
fun MovieListScreen(
    viewModel: MovieListViewModel,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    onMovieClick: (Int) -> Unit
) {
    // We no longer collect "state", but the pagination "movieFlow"
    val movies = viewModel.movieFlow.collectAsLazyPagingItems()

    var query by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            SearchBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = if (expanded) 0.dp else 16.dp),
                inputField = {
                    SearchBarDefaults.InputField(
                        query = query,
                        onQueryChange = { newText ->
                            query = newText
                            viewModel.onSearchQueryChange(newText)
                        },
                        onSearch = { expanded = false },
                        expanded = expanded,
                        onExpandedChange = { expanded = it },
                        placeholder = { Text("Search for a movie...") },
                        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                        trailingIcon = {
                            if (query.isNotEmpty()) {
                                IconButton(onClick = {
                                    query = ""
                                    viewModel.onSearchQueryChange("")
                                }) {
                                    Icon(Icons.Default.Close, contentDescription = "Clear")
                                }
                            }
                        }
                    )
                },
                expanded = expanded,
                onExpandedChange = { expanded = it }
            ) {
                // We pass the "movies" (LazyPagingItems) to the list
                MovieLazyList(movies, sharedTransitionScope, animatedVisibilityScope, onMovieClick)
            }
        }
    ) { paddingValues ->
        if (!expanded) {
            Box(modifier = Modifier.padding(paddingValues)) {
                MovieLazyList(movies, sharedTransitionScope, animatedVisibilityScope, onMovieClick)
            }
        }
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun MovieLazyList(
    movies: androidx.paging.compose.LazyPagingItems<com.rjenequin.tmdb.features.movielist.domain.Movie>,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    onMovieClick: (Int) -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {

        // Handle initial load (main Loading)
        if (movies.loadState.refresh is LoadState.Loading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        }

        // Handle main error
        val error = movies.loadState.refresh as? LoadState.Error
        error?.let {
            Text(
                text = "Error: ${it.error.localizedMessage}",
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.align(Alignment.Center)
            )
        }

        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Using the Paging-specific items method
            items(
                count = movies.itemCount,
                key = movies.itemKey { it.id }
            ) { index ->
                val movie = movies[index]
                if (movie != null) {
                    MovieItem(
                        movie = movie,
                        sharedTransitionScope = sharedTransitionScope,
                        animatedVisibilityScope = animatedVisibilityScope,
                        onClick = { onMovieClick(movie.id) }
                    )
                }
            }

            // Loading indicator for the next page (Append)
            if (movies.loadState.append is LoadState.Loading) {
                item {
                    Box(Modifier
                        .fillMaxWidth()
                        .padding(16.dp)) {
                        CircularProgressIndicator(Modifier.align(Alignment.Center))
                    }
                }
            }
        }
    }
}