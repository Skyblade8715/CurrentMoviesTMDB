package com.skycom.currentmoviestmdb.presentation.home.movies_adapter

import android.content.Context
import android.content.Intent
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.skycom.currentmoviestmdb.R
import com.skycom.currentmoviestmdb.databinding.MovieItemBinding
import com.skycom.currentmoviestmdb.domain.model.movie.Movie
import com.skycom.currentmoviestmdb.presentation.details.MovieDetailsActivity
import com.skycom.currentmoviestmdb.presentation.home.MainViewModel

class MovieViewHolder(
    binding: MovieItemBinding,
    private val context: Context,
    private val mainViewModel: MainViewModel,
) : RecyclerView.ViewHolder(binding.root) {

    private val card = binding.movieItemCard
    private val banner = binding.movieItemBanner
    private val date = binding.movieItemDate
    private val ratingIndicator = binding.movieItemRating
    private val favourite = binding.movieItemFavourite
    private val title = binding.movieItemTitle

    fun bind(movie: Movie) {
        title.text = movie.title
        date.text = movie.release_date

        if(movie.poster_path != null) {
            banner.load("https://image.tmdb.org/t/p/original/${movie.poster_path}")
        } else {
            banner.load(R.drawable.lacking_link_icon)
        }
        ratingIndicator.setProgress(movie.vote_average)

        favourite.setOnCheckedChangeListener(null)
        favourite.isChecked = movie.isLiked
        favourite.setOnCheckedChangeListener { _, isChecked ->
            mainViewModel.setLikedState(movie.id, isChecked)
        }
        card.setOnClickListener{
            context.startActivity(
                Intent(context, MovieDetailsActivity::class.java)
                    .putExtra("movieDetails", movie)
                    .putExtra("isLiked", favourite.isChecked)
                    .putExtra("position", absoluteAdapterPosition))
        }
    }
}

object MovieDiffCallback : DiffUtil.ItemCallback<Movie>() {
    override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
        return oldItem == newItem
    }
}