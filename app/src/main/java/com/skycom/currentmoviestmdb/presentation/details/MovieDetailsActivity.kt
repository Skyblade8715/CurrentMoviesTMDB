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
import com.skycom.currentmoviestmdb.domain.model.now_playing.Movie
import com.skycom.currentmoviestmdb.presentation.playing.NowPlayingViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MovieDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMovieDetailsBinding
    private lateinit var viewModel : MovieDetailsViewModel
    private lateinit var nowPlayingViewModel : NowPlayingViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMovieDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[MovieDetailsViewModel::class.java]
        nowPlayingViewModel = ViewModelProvider(this)[NowPlayingViewModel::class.java]

        val movie = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("movieDetails", Movie::class.java)
        } else {
            intent.getParcelableExtra("movieDetails")
        }

        viewModel.movieDetails.observe(this) { details ->
            details?.let {
                if (details.backdrop_path != null) {
                    binding.movieDetailsBanner.load("https://image.tmdb.org/t/p/original/${details.backdrop_path}") {
                        crossfade(true)
                        placeholder(getProgressDrawable())
                    }
                } else {
                    binding.movieDetailsBanner.load(R.drawable.lacking_link_icon)
                }
                binding.movieDetailsTitle.text = details.title
                binding.movieDetailsOverview.text = details.overview
                binding.movieDetailsDate.text = details.release_date
                binding.movieDetailsProgress.setProgress(details.vote_average)
                binding.movieDetailsLang.text =
                    details.spoken_languages.joinToString(separator = ", ") { it.english_name }
                binding.movieDetailsGenres.text =
                    details.genres.joinToString(separator = " - ") { it.name }

                binding.movieDetailsFavourite.isChecked = movie?.isLiked ?: false
                binding.movieDetailsFavourite.setOnCheckedChangeListener { _, isChecked ->
                    nowPlayingViewModel.setLikedState(details.id, isChecked)
                    sendMessage(isChecked)
                }
            }
        }

        viewModel.errorState.observe(this) { errorMessage ->
            if (!errorMessage.isNullOrEmpty()) {
                Toast.makeText(this@MovieDetailsActivity, errorMessage, Toast.LENGTH_SHORT).show()
            }
        }

        movie?.let { viewModel.fetchMovieDetails(movieId = it.id) }

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