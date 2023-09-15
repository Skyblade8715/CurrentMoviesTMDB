package com.skycom.currentmoviestmdb.data.repository

import com.skycom.currentmoviestmdb.data.source.TMDBApi
import com.skycom.currentmoviestmdb.domain.model.now_playing.NowPlayingMovies
import com.skycom.currentmoviestmdb.domain.model.searched_movie.SearchedMovie
import com.skycom.currentmoviestmdb.domain.repository.NowPlayingRepository
import retrofit2.Response
import javax.inject.Inject

class NowPlayingRepositoryImpl @Inject constructor(
    private val tmdbApi: TMDBApi
) : NowPlayingRepository {

    override suspend fun getNowPlaying(page: Int): Response<NowPlayingMovies> {
        return tmdbApi.getNowPlaying(page)
    }

    override suspend fun searchMovies(searchedString: String): Response<SearchedMovie> {
        return tmdbApi.searchMovies(searchedString)
    }
}