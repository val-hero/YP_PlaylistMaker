package com.example.playlistmaker.library.favourite.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.core.domain.model.Track
import com.example.playlistmaker.databinding.FragmentFavouriteTracksBinding
import com.example.playlistmaker.search.ui.TrackAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavouriteTracksFragment : Fragment() {
    private lateinit var binding: FragmentFavouriteTracksBinding
    private val viewModel by viewModel<FavouriteTracksFragmentViewModel>()

    private val adapter = TrackAdapter {
        openTrack(it)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFavouriteTracksBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.favTracksRecycler.adapter = adapter

        viewModel.uiState.observe(viewLifecycleOwner) { state ->
            render(state)
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.getFavouriteTracks()
    }

    private fun openTrack(track: Track) {
        viewModel.saveSelectedTrack(track)
        findNavController().navigate(R.id.action_libraryFragment_to_playerFragment)
    }

    private fun render(uiState: FavouriteTracksUiState) {
        when (uiState) {
            is FavouriteTracksUiState.Empty -> {
                showEmptyPlaceholder()
            }

            is FavouriteTracksUiState.Content -> {
                showTracks(uiState.data)
            }
        }
    }

    private fun showEmptyPlaceholder() {
        binding.favTracksRecycler.isVisible = false
        binding.emptyFavouritesPlaceholder.isVisible = true
    }

    private fun showTracks(tracks: List<Track>) {
        adapter.updateTracks(tracks)
        binding.favTracksRecycler.isVisible = true
        binding.emptyFavouritesPlaceholder.isVisible = false
    }


    companion object {
        fun newInstance() = FavouriteTracksFragment()
    }
}