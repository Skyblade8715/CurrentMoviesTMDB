package com.skycom.currentmoviestmdb.presentation.details

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import coil.load
import com.skycom.currentmoviestmdb.R
import com.skycom.currentmoviestmdb.databinding.ActivityMovieDetailsBinding
import com.skycom.currentmoviestmdb.domain.model.movie.Movie
import com.skycom.currentmoviestmdb.presentation.home.MainViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MovieDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMovieDetailsBinding
    private lateinit var viewModel : MovieDetailsViewModel
    private lateinit var mainViewModel : MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMovieDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[MovieDetailsViewModel::class.java]
        mainViewModel = ViewModelProvider(this)[MainViewModel::class.java]
        val movie = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("movieDetails", Movie::class.java)
        } else {
            intent.getParcelableExtra("movieDetails")
        }
        val isLiked = intent.getBooleanExtra("isLiked", false)

        setupFromIntent(movie, isLiked)
        setupFromAPI(isLiked)

        movie?.let { viewModel.fetchMovieDetails(movieId = it.id) }
    }

    private fun setupFromIntent(movie: Movie?, isLiked: Boolean) {
        movie?.let {
            binding.movieDetailsTitle.text = movie.title
            binding.movieDetailsOverview.text = movie.overview
            binding.movieDetailsDate.text = movie.release_date
            binding.movieDetailsProgress.setProgress(movie.vote_average)
            binding.movieDetailsLang.text =
                "Couldn't find language"
            binding.movieDetailsGenres.text =
                "Couldn't find genre"

            binding.movieDetailsFavourite.isChecked = isLiked
            binding.movieDetailsFavourite.setOnCheckedChangeListener { _, isChecked ->
                mainViewModel.setLikedState(movie.id, isChecked)
                sendMessage(isChecked)
            }
        }
    }

    private fun setupFromAPI(isLiked: Boolean) {

        viewModel.movieDetails.observe(this) { movieDetails ->
            movieDetails?.let {
                if (movieDetails.backdrop_path != null) {
                    binding.movieDetailsBanner.load("https://image.tmdb.org/t/p/original/${movieDetails.backdrop_path}") {
                        crossfade(true)
                        placeholder(getProgressDrawable())
                    }
                } else {
                    binding.movieDetailsBanner.load(R.drawable.lacking_link_icon)
                }
                binding.movieDetailsTitle.text = movieDetails.title
                binding.movieDetailsOverview.text = movieDetails.overview
                binding.movieDetailsDate.text = movieDetails.release_date
                binding.movieDetailsProgress.setProgress(movieDetails.vote_average)
                binding.movieDetailsLang.text =
                    movieDetails.spoken_languages?.joinToString(separator = ", ") { it.english_name }
                binding.movieDetailsGenres.text =
                    movieDetails.genres?.joinToString(separator = " - ") { it.name }

                binding.movieDetailsFavourite.isChecked = isLiked
                binding.movieDetailsFavourite.setOnCheckedChangeListener { _, isChecked ->
                    mainViewModel.setLikedState(movieDetails.id, isChecked)
                    sendMessage(isChecked)
                }
            }
        }

        viewModel.errorState.observe(this) { errorMessage ->
            if (!errorMessage.isNullOrEmpty()) {
                Toast.makeText(this@MovieDetailsActivity, errorMessage, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun getProgressDrawable(): Drawable {
        val circularProgressDrawable = CircularProgressDrawable(this)
        circularProgressDrawable.strokeWidth = 20f
        circularProgressDrawable.centerRadius = 100f
        circularProgressDrawable.backgroundColor = Color.TRANSPARENT
        circularProgressDrawable.start()
        return circularProgressDrawable
    }

    private fun sendMessage(isChecked: Boolean) {

        val position = intent.getIntExtra("position", -1)
        val intent = Intent("update_like")
        intent.putExtra("isChecked", isChecked)
        intent.putExtra("position", position)
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
    }

}