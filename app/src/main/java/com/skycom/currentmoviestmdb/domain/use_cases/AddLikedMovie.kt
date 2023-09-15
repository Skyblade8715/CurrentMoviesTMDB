package com.skycom.currentmoviestmdb.domain.use_cases

import com.skycom.currentmoviestmdb.domain.repository.LikedMoviesRepository

class AddLikedMovie(
    private val repository: LikedMoviesRepository
) {
    operator fun invoke(movieId: String) = repository.addLikedMovie(movieId)
}