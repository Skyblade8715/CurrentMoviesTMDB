package com.skycom.currentmoviestmdb.data.repository

import android.content.SharedPreferences
import com.skycom.currentmoviestmdb.data.source.LikedMoviesData
import com.skycom.currentmoviestmdb.domain.repository.LikedMoviesRepository
import javax.inject.Inject

class LikedMoviesRepositoryImpl @Inject constructor(
    private val sharedPreferences: SharedPreferences
) : LikedMoviesRepository {

    override fun addLikedMovie(movieId: String) {
        val likedMovies =
            sharedPreferences
                .getStringSet(LikedMoviesData.likedMoviesPref, null)
                ?.toMutableSet() ?: mutableSetOf()

        likedMovies.add(movieId)
        sharedPreferences.edit().putStringSet(LikedMoviesData.likedMoviesPref, likedMovies).apply()
    }

    override fun deleteLikedMovie(movieId: String) {
        val likedMovies =
            sharedPreferences
                .getStringSet(LikedMoviesData.likedMoviesPref, null)
                ?.toMutableSet() ?: mutableSetOf()

        likedMovies.remove(movieId)
        sharedPreferences.edit()
            .putStringSet(LikedMoviesData.likedMoviesPref, likedMovies).apply()
    }

    override fun getAllLikedMovies(): Set<String>? {
        return sharedPreferences.getStringSet(LikedMoviesData.likedMoviesPref, null)
    }
}