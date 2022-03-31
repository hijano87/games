package com.hijano.games.ui

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.hijano.games.model.Game

class GamesAdapter(
    private val onItemClick: (game: Game) -> Unit
) : PagingDataAdapter<Game, GameViewHolder>(GAMES_COMPARATOR) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameViewHolder {
        return GameViewHolder.from(parent) { position ->
            getItem(position)?.let { onItemClick(it) }
        }
    }

    override fun onBindViewHolder(holder: GameViewHolder, position: Int) {
        getItem(position)?.let { game ->
            holder.bind(game)
        }
    }

    companion object {
        private val GAMES_COMPARATOR = object : DiffUtil.ItemCallback<Game>() {
            override fun areItemsTheSame(oldItem: Game, newItem: Game): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Game, newItem: Game): Boolean {
                return oldItem == newItem
            }
        }
    }
}
