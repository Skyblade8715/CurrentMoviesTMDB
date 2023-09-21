package com.skycom.currentmoviestmdb.domain.use_cases

data class UseCaseMovies(
    val addLikedMovie: AddLikedMovie,
    val deleteLikedMovie: DeleteLikedMovie,
    val getAllLikedMovies: GetAllLikedMovies,
    val getMovieDetails: GetMovieDetails,
    val getNowPlayingMovies: GetNowPlayingMovies,
    val searchMovies: SearchMovies
)