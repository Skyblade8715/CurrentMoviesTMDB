package com.skycom.currentmoviestmdb.domain.use_cases

import com.skycom.currentmoviestmdb.domain.model.now_playing.NowPlayingMovies
import com.skycom.currentmoviestmdb.domain.repository.NowPlayingRepository

class GetNowPlaying(
    private val repository: NowPlayingRepository
) {
    suspend operator fun invoke(page: Int): Result<NowPlayingMovies?> {
        return try {
            val response = repository.getNowPlaying(page)
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