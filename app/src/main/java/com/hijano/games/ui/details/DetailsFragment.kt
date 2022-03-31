package com.hijano.games.ui.details

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.TypedValue
import android.view.View
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
import com.hijano.games.model.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

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
                    viewModel.game.collect {
                        binding.error.isVisible = it is Resource.Error
                        binding.retry.isVisible = it is Resource.Error
                        binding.progress.isVisible = it is Resource.Loading

                        if (it is Resource.Success) {
                            binding.collapsingToolbarLayout.title = it.data.name
                            binding.summary.text = it.data.summary
                            binding.summary.isVisible = it.data.summary != null
                            binding.storyline.text = it.data.storyline
                            binding.storyline.isVisible = it.data.storyline != null
                            Glide.with(this@DetailsFragment)
                                .load(
                                    imageBuilder(
                                        it.data.imageId ?: "nocover",
                                        ImageSize.SCREENSHOT_MEDIUM,
                                        ImageType.PNG
                                    )
                                )
                                .placeholder(
                                    ColorDrawable(
                                        resolveColor(com.google.android.material.R.attr.colorSurface)
                                    )
                                )
                                .centerCrop()
                                .into(binding.image)
                        }
                    }
                }
            }
        }
    }

    @ColorInt
    private fun resolveColor(@AttrRes color: Int): Int {
        return TypedValue().apply {
            requireContext().theme.resolveAttribute(color, this, true)
        }.data
    }
}
