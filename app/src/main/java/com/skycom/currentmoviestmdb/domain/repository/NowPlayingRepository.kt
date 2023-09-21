package com.skycom.currentmoviestmdb.domain.repository

import androidx.paging.PagingData
import com.skycom.currentmoviestmdb.domain.model.movie.Movie
import kotlinx.coroutines.flow.Flow

interface NowPlayingRepository {

    suspend fun getNowPlayingMovies() : Flow<PagingData<Movie>>
    suspend fun getSearchedMovies(searchedString: String) : Flow<PagingData<Movie>>
}