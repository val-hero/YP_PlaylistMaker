package com.example.playlistmaker.player.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentAddToPlaylistBottomsheetBinding
import com.example.playlistmaker.player.ui.viewmodel.BottomSheetViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class BottomSheetFragment : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentAddToPlaylistBottomsheetBinding
    private val viewModel by viewModel<BottomSheetViewModel>()

    private val adapter = BottomSheetAdapter {
        viewModel.saveToPlaylist(it)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddToPlaylistBottomsheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun getTheme(): Int {
        return R.style.CustomBottomSheetDialog
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.playlistsRecycler.adapter = adapter

        viewModel.playlists.observe(viewLifecycleOwner) {
            adapter.updatePlaylists(it)
        }
    }

    companion object {
        const val TAG = "AddToPlaylistBottomSheet"
    }
}