package com.example.playlistmaker.library.favourite.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.playlistmaker.databinding.FragmentFavouriteTracksBinding
import com.example.playlistmaker.search.ui.TrackAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavouriteTracksFragment: Fragment() {
    private lateinit var binding: FragmentFavouriteTracksBinding
    private val viewModel by viewModel<FavouriteTracksFragmentViewModel>()

    private val adapter = TrackAdapter {
        //findNavController().navigate(R.id.action_libraryFragment_to_playerFragment)
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

        viewModel.favouriteTracks.observe(viewLifecycleOwner) { tracks ->
            adapter.updateTracks(tracks)
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.getFavouriteTracks()
    }


    companion object {
        fun newInstance() = FavouriteTracksFragment()
    }
}