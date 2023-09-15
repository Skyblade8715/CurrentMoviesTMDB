package com.skycom.currentmoviestmdb.domain.use_cases

data class UseCaseMovies(
    val addLikedMovie: AddLikedMovie,
    val deleteLikedMovie: DeleteLikedMovie,
    val getAllLikedMovies: GetAllLikedMovies,
    val getMovieDetails: GetMovieDetails,
    val getNowPlaying: GetNowPlaying,
    val searchMovies: SearchMovies
)