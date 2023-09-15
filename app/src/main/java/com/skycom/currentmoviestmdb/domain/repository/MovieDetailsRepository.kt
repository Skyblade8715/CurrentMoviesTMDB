package com.skycom.currentmoviestmdb.domain.repository

import com.skycom.currentmoviestmdb.domain.model.movie_details.MovieDetails
import retrofit2.Response

interface MovieDetailsRepository {

    suspend fun getMovieDetailsById(movieId: Int) : Response<MovieDetails>
}