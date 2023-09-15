package com.skycom.currentmoviestmdb.data.source

interface LikedMoviesData {

    fun addMovie(movieId: Int)
    fun deleteMovie(movieId: Int)
    fun getMovies(): List<String>?

    companion object {
        const val likedMoviesPref = "LikedMovies"
    }
}