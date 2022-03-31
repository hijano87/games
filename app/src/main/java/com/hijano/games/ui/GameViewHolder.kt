package com.hijano.games.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.api.igdb.utils.ImageSize
import com.api.igdb.utils.ImageType
import com.api.igdb.utils.imageBuilder
import com.bumptech.glide.Glide
import com.hijano.games.R
import com.hijano.games.databinding.ItemGameBinding
import com.hijano.games.model.Game

class GameViewHolder(private val binding: ItemGameBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(game: Game) {
        Glide.with(itemView)
            .load(imageBuilder(game.imageId ?: "nocover", ImageSize.COVER_SMALL, ImageType.PNG))
            .placeholder(R.drawable.ic_baseline_image_24)
            .error(R.drawable.ic_baseline_error_24)
            .into(binding.image)
        binding.name.text = game.name
    }

    companion object {
        fun from(parent: ViewGroup, onItemClick: (Int) -> Unit): GameViewHolder {
            return GameViewHolder(
                ItemGameBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            ).apply {
                this.binding.root.setOnClickListener { onItemClick(bindingAdapterPosition) }
            }
        }
    }
}
