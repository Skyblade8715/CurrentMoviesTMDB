package com.skycom.currentmoviestmdb.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.skycom.currentmoviestmdb.data.mappers.toMovieList
import com.skycom.currentmoviestmdb.data.source.TMDBApi
import com.skycom.currentmoviestmdb.domain.model.movie.Movie

class MoviePagingSource (
    val api: TMDBApi,
    private val query: String?
) : PagingSource<Int, Movie>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {
        val pageNumber = params.key ?: 1
        return try {
            val response = if (query != null) {
                api.getSearchedMovies(pageNumber, query).body()?.toMovieList()
            } else {
                api.getNowPlayingMovies(pageNumber).body()?.toMovieList()
            }
            LoadResult.Page(
                data = response?.results.orEmpty(),
                prevKey = null,
                nextKey = (response?.page ?: pageNumber) + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Movie>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}