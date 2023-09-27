package com.example.playlistmaker.player.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentAddToPlaylistBottomsheetBinding
import com.example.playlistmaker.player.ui.viewmodel.PlayerBottomSheetViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlayerBottomSheetFragment : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentAddToPlaylistBottomsheetBinding
    private val viewModel by viewModel<PlayerBottomSheetViewModel>()

    private val adapter = PlayerBottomSheetAdapter {
        viewModel.saveToPlaylist(it) { isSaved ->
            makeToast(isSaved, it.name)
            if (isSaved)
                dismiss()
        }
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

        binding.newPlaylistButton.setOnClickListener {
            findNavController().navigate(R.id.action_playerFragment_to_createPlaylistFragment)
            dismiss()
        }

        viewModel.playlists.observe(viewLifecycleOwner) {
            adapter.updatePlaylists(it)
        }
    }

    private fun makeToast(savedSuccessfully: Boolean, playlistName: String) {
        Toast.makeText(
            requireContext(),
            if (savedSuccessfully) getString(R.string.playlist_added_message, playlistName)
            else getString(R.string.already_in_playlist_message, playlistName),
            Toast.LENGTH_LONG
        ).show()
    }

    companion object {
        const val TAG = "add_to_playlist_bottom_sheet"
    }
}