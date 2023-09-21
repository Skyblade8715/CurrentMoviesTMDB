package com.skycom.currentmoviestmdb.domain.model.now_playing

import com.skycom.currentmoviestmdb.domain.model.movie.Movie

data class NowPlayingMovies(
    val dates: Dates,
    val page: Int,
    val results: List<Movie>,
    val total_pages: Int,
    val total_results: Int
)