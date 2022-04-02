package com.hijano.games.ui.games

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.recyclerview.widget.RecyclerView
import com.hijano.games.databinding.ItemLoadBinding

class GameLoadStateViewHolder(
    private val binding: ItemLoadBinding,
    retry: () -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    init {
        binding.retry.setOnClickListener { retry() }
    }

    fun bind(loadState: LoadState) {
        binding.progress.isVisible = loadState is LoadState.Loading
        binding.retry.isVisible = loadState is LoadState.Error
        binding.error.isVisible = loadState is LoadState.Error
    }

    companion object {
        fun from(parent: ViewGroup, retry: () -> Unit): GameLoadStateViewHolder {
            val binding = ItemLoadBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            return GameLoadStateViewHolder(binding, retry)
        }
    }
}
