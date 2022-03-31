package com.hijano.games.ui.details

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
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
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.game.collect {
                        binding.error.isVisible = it is Resource.Error
                        binding.retry.isVisible = it is Resource.Error
                        binding.progress.isVisible = it is Resource.Loading

                        if (it is Resource.Success) {
                            Glide.with(this@DetailsFragment)
                                .load(
                                    imageBuilder(
                                        it.data.imageId ?: "nocover",
                                        ImageSize.SCREENSHOT_MEDIUM,
                                        ImageType.PNG
                                    )
                                )
                                .placeholder(R.drawable.ic_baseline_image_24)
                                .centerCrop()
                                .error(R.drawable.ic_baseline_error_24)
                                .into(binding.image)
                        }
                    }
                }
            }
        }
    }
}
