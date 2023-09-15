package com.skycom.currentmoviestmdb.domain.repository

import com.skycom.currentmoviestmdb.domain.model.now_playing.NowPlayingMovies
import com.skycom.currentmoviestmdb.domain.model.searched_movie.SearchedMovie
import retrofit2.Response

interface NowPlayingRepository {

    suspend fun getNowPlaying(page: Int) : Response<NowPlayingMovies>
    suspend fun searchMovies(searchedString: String) : Response<SearchedMovie>
}