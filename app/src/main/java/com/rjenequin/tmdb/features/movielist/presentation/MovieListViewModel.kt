package com.rjenequin.tmdb.features.movielist.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.rjenequin.tmdb.features.movielist.domain.Movie
import com.rjenequin.tmdb.features.movielist.domain.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
@HiltViewModel
class MovieListViewModel @Inject constructor(
    private val repository: MovieRepository
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    // We can still expose the query if the UI needs it for the SearchBar
    val searchQuery = _searchQuery.asStateFlow()

    /**
     * This is where the magic happens.
     * We transform the text flow into a flow of paginated data.
     */
    val movieFlow: Flow<PagingData<Movie>> = _searchQuery
        .debounce(500L)
        .distinctUntilChanged()
        .flatMapLatest { query ->
            when {
                // If empty -> We load the popular ones (Paginated)
                query.isBlank() -> repository.getMovies(null)

                // If < 3 characters -> We can either return empty,
                // or stay on the popular ones. Here, we stay on the popular ones.
                query.length < 3 -> repository.getMovies(null)

                // Otherwise -> Search (Paginated)
                else -> repository.getMovies(query)
            }
        }
        .cachedIn(viewModelScope) // ESSENTIAL to avoid reloading when returning from detail

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
    }
}