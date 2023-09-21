package com.skycom.currentmoviestmdb.domain.model.searched_movie

import com.skycom.currentmoviestmdb.domain.model.movie.Movie

data class SearchedMovie(
    val page: Int,
    val results: List<Movie>,
    val total_pages: Int,
    val total_results: Int
)