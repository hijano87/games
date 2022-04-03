package com.hijano.games.ui.details

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.util.TypedValue
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.AttrRes
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
import com.hijano.games.ui.ErrorMessage
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
                    viewModel.uiState.collect { uiState -> binding.bindState(uiState) }
                }
            }
        }
    }

    private fun FragmentDetailsBinding.bindState(uiState: DetailsUiState) {
        progress.isVisible = uiState.isLoading
        errorGroup.isVisible = !uiState.hasDetails && uiState.errorMessages.isEmpty()
        detailsGroup.isVisible = uiState.hasDetails
        uiState.game?.let { bindDetails(it) }
        if (uiState.errorMessages.isNotEmpty()) showError(uiState.errorMessages.first())
    }

    private fun FragmentDetailsBinding.bindDetails(game: Game) {
        collapsingToolbarLayout.title = game.name
        summary.text = game.summary
        summary.isVisible = game.summary != null
        storyline.text = game.storyline
        storyline.isVisible = game.storyline != null
        image.bindImage(game.imageId)
    }

    private fun showError(errorMessage: ErrorMessage) {
        Toast.makeText(requireContext(), errorMessage.messageId, Toast.LENGTH_LONG)
            .onHidden(errorMessage)
            .show()
    }

    private fun Toast.onHidden(errorMessage: ErrorMessage): Toast = apply {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            addCallback(
                object : Toast.Callback() {
                    override fun onToastHidden() {
                        viewModel.errorShown(errorMessage.id)
                    }
                }
            )
        }
    }

    private fun ImageView.bindImage(imageId: String?) {
        Glide.with(this@DetailsFragment)
            .load(imageBuilder(imageId ?: "nocover", ImageSize.SCREENSHOT_MEDIUM, ImageType.PNG))
            .placeholder(ColorDrawable(context.resolveAttr(MaterialR.attr.colorSurface)))
            .centerCrop()
            .into(this)
    }

    private fun Context.resolveAttr(@AttrRes attr: Int): Int = TypedValue().apply {
        theme.resolveAttribute(attr, this, true)
    }.data
}
