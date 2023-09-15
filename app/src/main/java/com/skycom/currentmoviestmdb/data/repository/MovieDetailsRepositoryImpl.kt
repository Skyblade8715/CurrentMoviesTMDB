package com.skycom.currentmoviestmdb.data.repository

import com.skycom.currentmoviestmdb.data.source.TMDBApi
import com.skycom.currentmoviestmdb.domain.model.movie_details.MovieDetails
import com.skycom.currentmoviestmdb.domain.repository.MovieDetailsRepository
import retrofit2.Response
import javax.inject.Inject

class MovieDetailsRepositoryImpl @Inject constructor(
    private val tmdbAPI: TMDBApi
): MovieDetailsRepository {

    override suspend fun getMovieDetailsById(movieId: Int): Response<MovieDetails> {
        return tmdbAPI.getMovieDetailsById(movieId)
    }
}