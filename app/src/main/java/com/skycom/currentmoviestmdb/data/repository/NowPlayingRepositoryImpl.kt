package com.skycom.currentmoviestmdb.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.skycom.currentmoviestmdb.data.paging.MoviePagingSource
import com.skycom.currentmoviestmdb.data.source.TMDBApi
import com.skycom.currentmoviestmdb.domain.model.movie.Movie
import com.skycom.currentmoviestmdb.domain.repository.NowPlayingRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class NowPlayingRepositoryImpl @Inject constructor(
    private val tmdbApi: TMDBApi
) : NowPlayingRepository {

    override suspend fun getNowPlayingMovies(): Flow<PagingData<Movie>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                MoviePagingSource(tmdbApi, null)
            }
        ).flow
    }

    override suspend fun getSearchedMovies(searchedString: String): Flow<PagingData<Movie>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                MoviePagingSource(tmdbApi, searchedString)
            }
        ).flow
    }
}