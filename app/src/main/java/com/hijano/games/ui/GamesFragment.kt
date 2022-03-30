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
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class GamesFragment : Fragment(R.layout.fragment_games) {
    private val viewModel: GamesViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentGamesBinding.bind(view)
        val gamesAdapter = GamesAdapter()
        val footerAdapter = GameLoadStateAdapter { gamesAdapter.retry() }

        binding.games.adapter = gamesAdapter.withLoadStateFooter(footerAdapter)
        binding.retry.setOnClickListener { gamesAdapter.retry() }
        binding.swipe.setOnRefreshListener { gamesAdapter.refresh() }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.games.collect { games ->
                        gamesAdapter.submitData(games)
                    }
                }
                launch {
                    gamesAdapter.loadStateFlow.collect { loadState ->
                        binding.empty.isVisible =
                            loadState.refresh is LoadState.NotLoading && gamesAdapter.itemCount == 0

                        binding.games.isVisible = loadState.source.refresh is LoadState.NotLoading
                                || loadState.mediator?.refresh is LoadState.NotLoading

                        binding.swipe.isRefreshing =
                            loadState.mediator?.refresh is LoadState.Loading

                        val isInitialError = loadState.mediator?.refresh is LoadState.Error
                                && gamesAdapter.itemCount == 0
                        binding.error.isVisible = isInitialError
                        binding.retry.isVisible = isInitialError
                    }
                }
            }
        }
    }
}
