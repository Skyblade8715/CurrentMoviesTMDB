package com.skycom.currentmoviestmdb.domain.model.now_playing

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Movie(
    val adult: Boolean,
    val backdrop_path: String?,
    val genre_ids: List<Int>,
    val id: Int,
    val original_language: String,
    val original_title: String,
    val overview: String,
    val popularity: Double,
    val poster_path: String?,
    val release_date: String,
    var title: String,
    val video: Boolean,
    val vote_average: Double,
    val vote_count: Int,
    var isLiked: Boolean = false
) : Parcelable