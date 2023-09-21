package com.skycom.currentmoviestmdb.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.skycom.currentmoviestmdb.domain.model.movie.Movie
import com.skycom.currentmoviestmdb.domain.use_cases.UseCaseMovies
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MainViewModel @Inject constructor(
    private val useCaseMovies: UseCaseMovies,
): ViewModel() {

    private val _moviesList: MutableStateFlow<PagingData<Movie>> = MutableStateFlow(value = PagingData.empty())
    val moviesList: MutableStateFlow<PagingData<Movie>> = _moviesList

    private var _moviesKeptBeforeSearch: MutableStateFlow<PagingData<Movie>> = _moviesList
    var firstSearch = true

    init {
        viewModelScope.launch {
            getNowPlayingMovies()
        }
    }
    private suspend fun getNowPlayingMovies() {
        val likedMovies: Set<String> = useCaseMovies.getAllLikedMovies() ?: emptySet()

        useCaseMovies.getNowPlayingMovies()
            .distinctUntilChanged()
            .map { movieList ->
                movieList.map { movie ->
                    movie.copy(isLiked = likedMovies.contains(movie.id.toString()))
                }
            }
            .cachedIn(viewModelScope)
            .collect {
                _moviesList.value = it
            }
    }

    fun setLikedState(movieId: Int, isLiked: Boolean) {
        if (isLiked) {
            useCaseMovies.addLikedMovie(movieId.toString())
        } else {
            useCaseMovies.deleteLikedMovie(movieId.toString())
        }
    }
    suspend fun resetSearch(){
        getNowPlayingMovies()
        firstSearch = true
    }

    suspend fun searchMovies(searchedString: String) {
        val likedMovies: Set<String> = useCaseMovies.getAllLikedMovies() ?: emptySet()
        viewModelScope.launch {
            if (firstSearch) {
                _moviesKeptBeforeSearch.value = _moviesList.value
            }
            useCaseMovies.searchMovies(searchedString)
                .distinctUntilChanged()
                .map { movieList ->
                    movieList.map { movie ->
                        movie.copy(isLiked = likedMovies.contains(movie.id.toString()))
                    }
                }
                .cachedIn(viewModelScope)
                .collect {
                    _moviesList.value = it
                }
        }
    }
}