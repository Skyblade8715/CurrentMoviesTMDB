package com.skycom.currentmoviestmdb.data.mappers

import com.skycom.currentmoviestmdb.domain.model.movie.Movie
import com.skycom.currentmoviestmdb.domain.model.movie.MovieList
import com.skycom.currentmoviestmdb.domain.model.movie_details.MovieDetails
import com.skycom.currentmoviestmdb.domain.model.now_playing.NowPlayingMovies
import com.skycom.currentmoviestmdb.domain.model.searched_movie.SearchedMovie

fun NowPlayingMovies.toMovieList(): MovieList {
    return MovieList(
        results = results,
        page = page,
        total_pages = total_pages,
    )
}

fun SearchedMovie.toMovieList(): MovieList {
    return MovieList(
        results = results,
        page = page,
        total_pages = total_pages,
    )
}

//This is used in movie details, to present as much information as possible
fun Movie.toDetailMovie(): MovieDetails {
    return MovieDetails(
        adult = adult,
        backdrop_path = backdrop_path,
        belongs_to_collection = null,
        budget = null,
        genres = null,
        homepage = null,
        id = id,
        imdb_id = null,
        original_language = original_language,
        original_title = original_title,
        overview = overview,
        popularity = popularity,
        poster_path = poster_path,
        production_companies = null,
        production_countries = null,
        release_date = release_date,
        revenue = null,
        runtime = null,
        spoken_languages = null,
        status = null,
        tagline = null,
        title = title,
        video = video,
        vote_average = vote_average,
        vote_count = vote_count
    )
}