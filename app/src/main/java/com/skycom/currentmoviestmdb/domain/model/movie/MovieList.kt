package com.skycom.currentmoviestmdb.domain.model.movie

data class MovieList(
    val results: List<Movie>,
    val page: Int,
    val total_pages: Int,
)