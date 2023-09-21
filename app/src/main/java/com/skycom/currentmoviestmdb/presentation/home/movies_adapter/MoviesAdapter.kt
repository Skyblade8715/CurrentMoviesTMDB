package com.skycom.currentmoviestmdb.presentation.home.movies_adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.skycom.currentmoviestmdb.databinding.MovieItemBinding
import com.skycom.currentmoviestmdb.domain.model.movie.Movie
import com.skycom.currentmoviestmdb.presentation.home.MainViewModel
import javax.inject.Inject

class MoviesAdapter @Inject constructor(
    private val context: Context,
    private val mainViewModel: MainViewModel,
    diffCallback: DiffUtil.ItemCallback<Movie>
) : PagingDataAdapter<Movie, MovieViewHolder>(diffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val binding = MovieItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MovieViewHolder(binding, context, mainViewModel)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }
}