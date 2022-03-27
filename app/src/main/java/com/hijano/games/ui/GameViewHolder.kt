package com.hijano.games.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hijano.games.databinding.ItemGameBinding
import com.hijano.games.model.Game

class GameViewHolder(private val binding: ItemGameBinding) : RecyclerView.ViewHolder(binding.root) {
    companion object {
        fun from(parent: ViewGroup): GameViewHolder {
            return GameViewHolder(
                ItemGameBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }
    }

    fun bind(game: Game) {
        binding.name.text = game.name
    }
}