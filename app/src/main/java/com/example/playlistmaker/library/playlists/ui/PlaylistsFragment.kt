package com.example.playlistmaker.library.playlists.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentPlaylistsBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistsFragment : Fragment() {
    private lateinit var binding: FragmentPlaylistsBinding
    private val viewModel by viewModel<PlaylistsFragmentViewModel>()
    private val adapter = PlaylistAdapter { }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPlaylistsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.addPlaylistButton.setOnClickListener {
            findNavController().navigate(R.id.action_libraryFragment_to_createPlaylistFragment)
        }

        binding.playlistsRecycler.adapter = adapter

        viewModel.playlists.observe(viewLifecycleOwner) { playlists ->
            playlists?.let {
                adapter.updatePlaylists(it)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.fetchPlaylists()
    }


    companion object {
        fun newInstance() = PlaylistsFragment()
    }
}