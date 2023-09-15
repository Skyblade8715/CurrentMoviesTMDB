package com.skycom.currentmoviestmdb.domain.use_cases

import com.skycom.currentmoviestmdb.domain.model.movie_details.MovieDetails
import com.skycom.currentmoviestmdb.domain.repository.MovieDetailsRepository

class GetMovieDetails(
    private val repository: MovieDetailsRepository
) {
    suspend operator fun invoke(movieId: Int): Result<MovieDetails?> {
        return try {
            val response = repository.getMovieDetailsById(movieId)
            if (response.isSuccessful) {
                Result.success(response.body())
            } else {
                Result.failure(Exception("Failed to connect with the server."))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}