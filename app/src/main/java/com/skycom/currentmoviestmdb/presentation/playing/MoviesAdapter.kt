package com.skycom.currentmoviestmdb.presentation.playing

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.skycom.currentmoviestmdb.R
import com.skycom.currentmoviestmdb.databinding.MovieItemBinding
import com.skycom.currentmoviestmdb.domain.model.now_playing.Movie
import com.skycom.currentmoviestmdb.presentation.details.MovieDetailsActivity

class MoviesAdapter(
    var movieList: List<Movie>,
    private val context: Context,
    private val nowPlayingViewModel: NowPlayingViewModel,
    private val recyclerView: RecyclerView
) :
    RecyclerView.Adapter<MoviesAdapter.MovieViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val binding = MovieItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MovieViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.bind(movieList[position])
    }

    fun setData(newMovies: List<Movie>) {
        if(newMovies.isNotEmpty()) {
            movieList = newMovies
            notifyDataSetChanged()
            recyclerView.scrollToPosition(0)
            return
        }
        if(nowPlayingViewModel.checkForEmptyAdapter) {
            Toast.makeText(
                context, "No movies matching search criteria",
                Toast.LENGTH_SHORT
            ).show()
            nowPlayingViewModel.checkForEmptyAdapter = false
        }
        return
    }

    override fun getItemCount() = movieList.size

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    inner class MovieViewHolder(
        binding: MovieItemBinding
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
                nowPlayingViewModel.setLikedState(movie.id, isChecked)
            }
            card.setOnClickListener{
                context.startActivity(
                    Intent(context, MovieDetailsActivity::class.java)
                        .putExtra("movieDetails", movie)
                        .putExtra("position", absoluteAdapterPosition))
            }
        }
    }
}