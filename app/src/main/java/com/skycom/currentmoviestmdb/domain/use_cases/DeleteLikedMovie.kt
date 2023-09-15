package com.skycom.currentmoviestmdb.domain.use_cases

import com.skycom.currentmoviestmdb.domain.repository.LikedMoviesRepository

class DeleteLikedMovie(
    private val repository: LikedMoviesRepository
){
    operator fun invoke(movieId: String) = repository.deleteLikedMovie(movieId)
}