package com.skycom.currentmoviestmdb.domain.model.now_playing

data class NowPlayingMovies(
    val dates: Dates,
    val page: Int,
    val results: List<Movie>,
    val total_pages: Int,
    val total_results: Int
)