package com.hijano.games.ui.games

import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter

class GameLoadStateAdapter(
    private val retry: () -> Unit
) : LoadStateAdapter<GameLoadStateViewHolder>() {
    override fun onBindViewHolder(holder: GameLoadStateViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        loadState: LoadState
    ): GameLoadStateViewHolder {
        return GameLoadStateViewHolder.from(parent, retry)
    }
}
