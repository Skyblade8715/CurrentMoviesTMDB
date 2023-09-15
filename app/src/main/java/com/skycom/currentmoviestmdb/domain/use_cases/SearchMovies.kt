package com.skycom.currentmoviestmdb.domain.use_cases

import com.skycom.currentmoviestmdb.domain.model.searched_movie.SearchedMovie
import com.skycom.currentmoviestmdb.domain.repository.NowPlayingRepository

class SearchMovies(
    private val repository: NowPlayingRepository
) {
    suspend operator fun invoke(searchedString: String) : Result<SearchedMovie?>  {
        return try {
            val response = repository.searchMovies(searchedString)
            if (response.isSuccessful)
                Result.success(response.body())
            else {
                Result.failure(Exception("Failed to connect with the server."))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}