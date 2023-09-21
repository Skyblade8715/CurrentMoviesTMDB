package com.skycom.currentmoviestmdb.presentation.home

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.skycom.currentmoviestmdb.databinding.ActivityMainBinding
import com.skycom.currentmoviestmdb.presentation.home.load_adapter.MovieLoadStateAdapter
import com.skycom.currentmoviestmdb.presentation.home.movies_adapter.MovieDiffCallback
import com.skycom.currentmoviestmdb.presentation.home.movies_adapter.MoviesAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var mainViewModel : MainViewModel
    private lateinit var moviesAdapter: MoviesAdapter
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mainViewModel = ViewModelProvider(this)[MainViewModel::class.java]

        binding.mainRecyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView = binding.mainRecyclerView
        moviesAdapter = MoviesAdapter(this@MainActivity, mainViewModel, MovieDiffCallback)

        recyclerView.adapter = moviesAdapter.withLoadStateFooter(
            footer = MovieLoadStateAdapter { moviesAdapter.retry() }
        )

        binding.mainRetryButton.setOnClickListener {
            moviesAdapter.retry()
        }

        setupSearchFeature()

        lifecycleScope.launch {
            mainViewModel.moviesList.collectLatest { pagingData ->
                moviesAdapter.submitData(pagingData)
            }
        }

        lifecycleScope.launch {
            moviesAdapter.loadStateFlow.collect { loadState ->
                val isListEmpty =
                    loadState.refresh is LoadState.NotLoading && moviesAdapter.itemCount == 0
                binding.mainProgressBar.isVisible = loadState.source.refresh is LoadState.Loading
                binding.mainRecyclerView.isVisible = !isListEmpty
                binding.mainRetryButton.isVisible = loadState.source.refresh is LoadState.Error

                val errorState = loadState.source.append as? LoadState.Error
                    ?: loadState.source.prepend as? LoadState.Error
                    ?: loadState.append as? LoadState.Error
                    ?: loadState.prepend as? LoadState.Error
                    ?: loadState.refresh as? LoadState.Error
                errorState?.let {
                    Toast.makeText(
                        this@MainActivity,
                        "\uD83D\uDE28 Wooops ${it.error}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

        LocalBroadcastManager.getInstance(this).registerReceiver(
            mMessageReceiver,
            IntentFilter("update_like")
        )
    }

    private fun setupSearchFeature() {
        binding.mainSearchInput.setOnEditorActionListener { _, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH && event?.action == KeyEvent.ACTION_DOWN) {
                if(binding.mainSearchInput.text.isNullOrBlank() && event.action == KeyEvent.ACTION_DOWN) {
                    Toast.makeText(
                        this@MainActivity, "Please, provided name of the movie",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {

                    lifecycleScope.launch {
                        mainViewModel.searchMovies(binding.mainSearchInput.text.toString())
                        mainViewModel.moviesList.collectLatest { pagingData ->
                            moviesAdapter.submitData(pagingData)
                        }
                        recyclerView.scrollToPosition(0)
                    }
                    hideKeyboard(this)
                }
            }
            false
        }

        binding.mainSearchLayout.setEndIconOnClickListener {
            hideKeyboard(this)
            binding.mainSearchInput.text?.clear()
            lifecycleScope.launch {
                mainViewModel.resetSearch()
            }
            recyclerView.scrollToPosition(0)
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
                val currentPagingData = moviesAdapter.snapshot().items
                val updatedItem = currentPagingData[pos].copy(isLiked = isChecked)

                val updatedList = currentPagingData.toMutableList()
                updatedList[pos] = updatedItem
                val updatedPagingData = PagingData.from(updatedList)

                moviesAdapter.submitData(lifecycle, updatedPagingData)
                moviesAdapter.notifyItemChanged(pos)
            }
        }
    }

    override fun onDestroy() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver)
        super.onDestroy()
    }
}