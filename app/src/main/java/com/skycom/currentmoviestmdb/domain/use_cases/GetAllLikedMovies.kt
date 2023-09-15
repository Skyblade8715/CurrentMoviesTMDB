package com.skycom.currentmoviestmdb.domain.use_cases

import com.skycom.currentmoviestmdb.domain.repository.LikedMoviesRepository

class GetAllLikedMovies(
    private val repository: LikedMoviesRepository
){
    operator fun invoke(): Set<String>? = repository.getAllLikedMovies()
}