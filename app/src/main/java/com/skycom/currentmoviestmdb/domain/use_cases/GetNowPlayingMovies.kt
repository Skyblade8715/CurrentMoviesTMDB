package com.skycom.currentmoviestmdb.domain.use_cases

import androidx.paging.PagingData
import com.skycom.currentmoviestmdb.domain.model.movie.Movie
import com.skycom.currentmoviestmdb.domain.repository.NowPlayingRepository
import kotlinx.coroutines.flow.Flow

class GetNowPlayingMovies(
    private val repository: NowPlayingRepository
) {
    suspend operator fun invoke(): Flow<PagingData<Movie>> = repository.getNowPlayingMovies()
}