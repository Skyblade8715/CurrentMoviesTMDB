package com.skycom.currentmoviestmdb.presentation.playing

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.skycom.currentmoviestmdb.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var nowPlayingViewModel : NowPlayingViewModel
    private lateinit var moviesAdapter: MoviesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        nowPlayingViewModel = ViewModelProvider(this)[NowPlayingViewModel::class.java]

        binding.mainRecyclerView.layoutManager = LinearLayoutManager(this)
        moviesAdapter = MoviesAdapter(
            mutableListOf(),
            this@MainActivity, nowPlayingViewModel, binding.mainRecyclerView
        )
        binding.mainRecyclerView.adapter = moviesAdapter


        setupSearchFeature()

        nowPlayingViewModel.nowPlayingMovies.observe(this) { mergedData ->
            mergedData.let {
                moviesAdapter.setData(it)
            }
        }
        nowPlayingViewModel.loadingState.observe(this) { isLoading ->
            if (isLoading) {
                binding.mainProgressBar.visibility = View.VISIBLE
            } else {
                binding.mainProgressBar.visibility = View.GONE
            }
        }

        nowPlayingViewModel.errorState.observe(this) { errorMessage ->
            if (!errorMessage.isNullOrEmpty()) {
                Toast.makeText(this@MainActivity, errorMessage, Toast.LENGTH_SHORT).show()
            }
        }
        nowPlayingViewModel.fetchNowPlayingMovies()

        LocalBroadcastManager.getInstance(this).registerReceiver(
            mMessageReceiver,
            IntentFilter("update_like")
        )
    }

    private fun setupSearchFeature() {

        //Setting up "Search"
        binding.mainSearchInput.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                if(binding.mainSearchInput.text.isNullOrBlank()) {
                    Toast.makeText(
                        this@MainActivity, "Please, provided name of the movie",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    nowPlayingViewModel.checkForEmptyAdapter = true
                    nowPlayingViewModel.searchMovies(binding.mainSearchInput.text.toString())
                    hideKeyboard(this)
                }
            }
            false
        }

        //Setting up Clear text
        binding.mainSearchLayout.setEndIconOnClickListener {
            hideKeyboard(this)
            binding.mainSearchInput.text?.clear()
            nowPlayingViewModel.resetSearch()
        }
    }

    private fun hideKeyboard(activity: Activity) {
        val imm = activity.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        var view = activity.currentFocus
        if (view == null) {
            view = View(activity)
        }
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    private val mMessageReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val isChecked = intent.getBooleanExtra("isChecked", true)
            val pos = intent.getIntExtra("position", -1)
            if(pos != -1) {
                moviesAdapter.movieList[pos].isLiked = isChecked
                moviesAdapter.notifyItemChanged(pos)
            }
        }
    }

    override fun onDestroy() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver)
        super.onDestroy()
    }
}