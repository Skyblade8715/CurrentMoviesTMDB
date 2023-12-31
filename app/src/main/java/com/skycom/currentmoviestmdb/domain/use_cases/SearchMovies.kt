package com.skycom.currentmoviestmdb.domain.use_cases

import androidx.paging.PagingData
import com.skycom.currentmoviestmdb.domain.model.movie.Movie
import com.skycom.currentmoviestmdb.domain.repository.NowPlayingRepository
import kotlinx.coroutines.flow.Flow

class SearchMovies(
    private val repository: NowPlayingRepository

) {
    suspend operator fun invoke(searchedString: String) : Flow<PagingData<Movie>> =
        repository.getSearchedMovies(searchedString)
}