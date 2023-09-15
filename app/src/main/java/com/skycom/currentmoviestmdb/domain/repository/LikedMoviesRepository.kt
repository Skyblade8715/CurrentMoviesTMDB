package com.skycom.currentmoviestmdb.domain.repository

interface LikedMoviesRepository {

    fun addLikedMovie(movieId: String)
    fun deleteLikedMovie(movieId: String)
    fun getAllLikedMovies(): Set<String>?
}
