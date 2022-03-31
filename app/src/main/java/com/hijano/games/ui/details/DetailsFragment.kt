package com.hijano.games.ui.details

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.TypedValue
import android.view.View
import android.widget.ImageView
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.api.igdb.utils.ImageSize
import com.api.igdb.utils.ImageType
import com.api.igdb.utils.imageBuilder
import com.bumptech.glide.Glide
import com.hijano.games.R
import com.hijano.games.databinding.FragmentDetailsBinding
import com.hijano.games.model.Game
import com.hijano.games.model.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import com.google.android.material.R as MaterialR

@AndroidEntryPoint
class DetailsFragment : Fragment(R.layout.fragment_details) {
    private val viewModel: DetailsViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentDetailsBinding.bind(view)
        binding.toolbar.setNavigationOnClickListener { findNavController().popBackStack() }
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.game.collect { game -> binding.bindState(game) }
                }
            }
        }
    }

    private fun FragmentDetailsBinding.bindState(resource: Resource<Game>) {
        progress.isVisible = resource is Resource.Loading
        errorGroup.isVisible = resource is Resource.Error
        detailsGroup.isVisible = (resource is Resource.Success)
        (resource as? Resource.Success)?.data?.let { game -> bindDetails(game) }
    }

    private fun FragmentDetailsBinding.bindDetails(game: Game) {
        collapsingToolbarLayout.title = game.name
        summary.text = game.summary
        summary.isVisible = game.summary != null
        storyline.text = game.storyline
        storyline.isVisible = game.storyline != null
        image.bindImage(game.imageId)
    }

    private fun ImageView.bindImage(imageId: String?) {
        Glide.with(this@DetailsFragment)
            .load(imageBuilder(imageId ?: "nocover", ImageSize.SCREENSHOT_MEDIUM, ImageType.PNG))
            .placeholder(ColorDrawable(resolveColor(MaterialR.attr.colorSurface)))
            .centerCrop()
            .into(this)
    }

    @ColorInt
    private fun resolveColor(@AttrRes color: Int): Int {
        return TypedValue().apply {
            requireContext().theme.resolveAttribute(color, this, true)
        }.data
    }
}
