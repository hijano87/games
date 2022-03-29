package com.hijano.games.ui

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.paging.LoadState
import com.hijano.games.R
import com.hijano.games.databinding.FragmentGamesBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class GamesFragment : Fragment(R.layout.fragment_games) {
    private val viewModel: GamesViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentGamesBinding.bind(view)
        val gamesAdapter = GamesAdapter()
        val headerAdapter = GameLoadStateAdapter { gamesAdapter.retry() }
        val footerAdapter = GameLoadStateAdapter { gamesAdapter.retry() }

        binding.games.adapter = gamesAdapter.withLoadStateHeaderAndFooter(
            headerAdapter, footerAdapter
        )

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.games.collect { games ->
                        gamesAdapter.submitData(games)
                    }
                }
                launch {
                    gamesAdapter.loadStateFlow.collect { loadState ->
                        headerAdapter.loadState = loadState.mediator
                            ?.refresh
                            ?.takeIf { it is LoadState.Error && gamesAdapter.itemCount > 0 }
                            ?: loadState.prepend

                        binding.empty.isVisible =
                            loadState.refresh is LoadState.NotLoading && gamesAdapter.itemCount == 0
                    }
                }
            }
        }
    }
}
