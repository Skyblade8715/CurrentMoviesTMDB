package com.skycom.currentmoviestmdb.presentation.home.load_adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.recyclerview.widget.RecyclerView
import com.skycom.currentmoviestmdb.R
import com.skycom.currentmoviestmdb.databinding.LoadStateFooterBinding

class LoadStateViewHolder (
    private val binding: LoadStateFooterBinding,
    retry: () -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    init {
        binding.loaderRetryButton.setOnClickListener { retry() }
    }

    fun bind(loadState: LoadState) {
        if (loadState is LoadState.Error) {
            binding.loaderTimeoutText.text = loadState.error.localizedMessage
        }
        binding.loaderProgressBar.isVisible = loadState is LoadState.Loading
        binding.loaderRetryButton.isVisible = loadState is LoadState.Error
        binding.loaderTimeoutText.isVisible = loadState is LoadState.Error
    }

    companion object {
        fun create(parent: ViewGroup, retry: () -> Unit): LoadStateViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.load_state_footer, parent, false)
            val binding = LoadStateFooterBinding.bind(view)
            return LoadStateViewHolder(binding, retry)
        }
    }
}