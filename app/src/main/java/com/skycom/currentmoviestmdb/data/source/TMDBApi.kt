package com.skycom.currentmoviestmdb.data.source

import com.skycom.currentmoviestmdb.domain.model.movie_details.MovieDetails
import com.skycom.currentmoviestmdb.domain.model.now_playing.NowPlayingMovies
import com.skycom.currentmoviestmdb.domain.model.searched_movie.SearchedMovie
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TMDBApi {

    @GET("movie/now_playing")
    suspend fun getNowPlaying(@Query("page") page: Int) : Response<NowPlayingMovies>

    @GET("movie/{id}")
    suspend fun getMovieDetailsById(@Path("id") movieId: Int) : Response<MovieDetails>

    @GET("search/movie")
    suspend fun searchMovies(@Query("query") searchedString: String) : Response<SearchedMovie>
}