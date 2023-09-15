package com.skycom.currentmoviestmdb.presentation.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.skycom.currentmoviestmdb.domain.model.movie_details.MovieDetails
import com.skycom.currentmoviestmdb.domain.use_cases.UseCaseMovies
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class MovieDetailsViewModel @Inject constructor(
    private val useCaseMovies: UseCaseMovies
): ViewModel() {


    private val _movieDetails = MutableLiveData<MovieDetails>()
    val movieDetails: LiveData<MovieDetails> get() = _movieDetails

    val loadingState = MutableLiveData<Boolean>()
    val errorState = MutableLiveData<String>()


    fun fetchMovieDetails(movieId: Int) {
        loadingState.value = true
        viewModelScope.launch {
            try {
                val temp = useCaseMovies.getMovieDetails(movieId)
                if (temp.isSuccess) {
                    _movieDetails.value = temp.getOrNull()
                }
            } catch (e: Exception) {
                errorState.value = "An error occurred"
            } finally {
                loadingState.value = false
            }
        }
    }
}