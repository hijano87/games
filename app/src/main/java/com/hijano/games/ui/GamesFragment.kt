package com.hijano.games.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.hijano.games.R
import com.hijano.games.databinding.FragmentGamesBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class GamesFragment : Fragment(R.layout.fragment_games) {
    private val viewModel: GamesViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = GamesAdapter()
        val binding = FragmentGamesBinding.bind(view)
        binding.games.adapter = adapter
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.games.collect { games ->
                        adapter.submitList(games)
                    }
                }
            }
        }
    }
}
