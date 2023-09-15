package com.skycom.currentmoviestmdb.presentation.playing

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.skycom.currentmoviestmdb.domain.model.now_playing.Movie
import com.skycom.currentmoviestmdb.domain.use_cases.UseCaseMovies
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class NowPlayingViewModel @Inject constructor(
    private val useCaseMovies: UseCaseMovies
): ViewModel() {

    private val _nowPlayingMovies = MutableLiveData<List<Movie>>()
    val nowPlayingMovies: LiveData<List<Movie>> = _nowPlayingMovies

    private var _moviesKeptBeforeSearch = MutableLiveData<List<Movie>>()

    val loadingState = MutableLiveData<Boolean>()
    val errorState = MutableLiveData<String>()
    var checkForEmptyAdapter = true
    var firstSearch = true


    fun fetchNowPlayingMovies(page: Int = 1) {
        loadingState.value = true
        viewModelScope.launch {
            try {
                val temp = useCaseMovies.getNowPlaying(page)
                if (temp.isSuccess) {
                    useCaseMovies.getAllLikedMovies()?.forEach {
                            movie -> temp.getOrNull()?.results?.find {
                            x -> x.id.toString() == movie }?.isLiked = true
                    }
                    _nowPlayingMovies.value = temp.getOrNull()?.results
                }
            } catch (e: Exception) {
                errorState.value = "An error occurred"
            } finally {
                loadingState.value = false
            }
        }
    }

    fun resetSearch(){
        _moviesKeptBeforeSearch.value?.let { moviesBeforeSearch ->
            _nowPlayingMovies.value = moviesBeforeSearch.toList()
        }
        firstSearch = true
    }

    fun searchMovies(searchedString: String) {
        loadingState.value = true
        viewModelScope.launch {
            if(firstSearch) {
                _moviesKeptBeforeSearch.value = _nowPlayingMovies.value?.toList()
            }
            try {
                val temp = useCaseMovies.searchMovies(searchedString)
                if (temp.isSuccess) {
                    useCaseMovies.getAllLikedMovies()?.forEach {
                            movie -> temp.getOrNull()?.results?.find {
                            x -> x.id.toString() == movie }?.isLiked = true
                    }
                    _nowPlayingMovies.value = temp.getOrNull()?.results
                    if(temp.getOrNull()?.results?.isNotEmpty() == true){
                        firstSearch = false
                    }
                }
            } catch (e: Exception) {
                errorState.value = "An error occurred"
            } finally {
                loadingState.value = false
            }
        }
    }

    fun setLikedState(movieId: Int, isLiked: Boolean) {
        if (isLiked) {
            useCaseMovies.addLikedMovie(movieId.toString())
        } else {
            useCaseMovies.deleteLikedMovie(movieId.toString())
        }
    }
}