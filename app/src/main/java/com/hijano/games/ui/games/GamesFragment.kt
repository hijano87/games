package com.hijano.games.ui.games

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.recyclerview.widget.DividerItemDecoration
import com.hijano.games.R
import com.hijano.games.databinding.FragmentGamesBinding
import com.hijano.games.model.Game
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class GamesFragment : Fragment(R.layout.fragment_games) {
    private val viewModel: GamesViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentGamesBinding.bind(view)
        val gamesAdapter = GamesAdapter { navigateToGameDetails(it) }
        val footerAdapter = GameLoadStateAdapter { gamesAdapter.retry() }
        val dividerItemDecoration = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)

        binding.games.addItemDecoration(dividerItemDecoration)
        binding.games.adapter = gamesAdapter.withLoadStateFooter(footerAdapter)
        binding.retry.setOnClickListener { gamesAdapter.retry() }
        binding.swipe.setOnRefreshListener { gamesAdapter.refresh() }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.games.collectLatest { games ->
                        gamesAdapter.submitData(games)
                    }
                }
                launch {
                    gamesAdapter.loadStateFlow.collect { loadState ->
                        val itemCount = gamesAdapter.itemCount
                        binding.empty.isVisible = isEmptyVisible(loadState, itemCount)
                        binding.games.isVisible = isListVisible(loadState)
                        binding.swipe.isRefreshing = isLoadingVisible(loadState)
                        binding.errorGroup.isVisible = isInitialError(loadState, itemCount)
                    }
                }
            }
        }
    }

    private fun navigateToGameDetails(game: Game) {
        findNavController().navigate(
            GamesFragmentDirections.actionGamesFragmentToDetailsFragment(game.id)
        )
    }

    private fun isEmptyVisible(loadState: CombinedLoadStates, itemCount: Int): Boolean {
        return loadState.refresh is LoadState.NotLoading && itemCount == 0
    }

    private fun isListVisible(loadState: CombinedLoadStates): Boolean {
        return loadState.source.refresh is LoadState.NotLoading ||
            loadState.mediator?.refresh is LoadState.NotLoading
    }

    private fun isLoadingVisible(loadState: CombinedLoadStates): Boolean {
        return loadState.mediator?.refresh is LoadState.Loading
    }

    private fun isInitialError(loadState: CombinedLoadStates, itemCount: Int): Boolean {
        return loadState.mediator?.refresh is LoadState.Error && itemCount == 0
    }
}
