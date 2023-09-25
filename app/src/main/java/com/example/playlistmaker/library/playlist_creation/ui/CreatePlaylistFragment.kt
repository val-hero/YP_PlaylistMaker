package com.example.playlistmaker.library.playlist_creation.ui

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toUri
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentCreatePlaylistBinding
import com.example.playlistmaker.library.playlists.domain.model.Playlist
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.koin.androidx.viewmodel.ext.android.viewModel

class CreatePlaylistFragment : Fragment() {
    private lateinit var binding: FragmentCreatePlaylistBinding
    private val viewModel by viewModel<CreatePlaylistViewModel>()
    private lateinit var onUnsavedExitDialog: MaterialAlertDialogBuilder
    private var imageUri: Uri? = null
    private var playlistId: Long? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCreatePlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        playlistId = arguments?.getLong(PLAYLIST_ID)
        if (playlistId != null) {
            viewModel.getPlaylistDetails(playlistId!!)
            viewModel.playlist.observe(viewLifecycleOwner) {
                it?.let {
                    imageUri = it.image?.toUri()
                    setupViews(it)
                }
            }
        }

        binding.createPlaylistToolbar.setNavigationOnClickListener {
            if (checkFilledViews()) {
                onUnsavedExitDialog.show()
            } else {
                findNavController().navigateUp()
            }
        }

        binding.playlistNameInputText.doOnTextChanged { text, _, _, _ ->
            binding.createPlaylistButton.isEnabled = !text.isNullOrBlank()
        }

        val selectMedia =
            registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
                uri?.let {
                    imageUri = it
                    binding.selectImageView.setImageURI(it)
                }
            }

        binding.selectImageCard.setOnClickListener {
            selectMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        binding.createPlaylistButton.setOnClickListener {
            if (playlistId != null) {
                viewModel.updatePlaylist(
                    name = binding.playlistNameInputText.text.toString(),
                    description = binding.playlistDescriptionText.text.toString(),
                    image = imageUri.toString()
                ) {
                    findNavController().navigateUp()
                }
            } else {
                viewModel.createPlaylist(
                    name = binding.playlistNameInputText.text.toString(),
                    description = binding.playlistDescriptionText.text.toString(),
                    image = imageUri.toString()
                ) {
                    findNavController().navigateUp()
                    makeSuccessToast(binding.playlistNameInputText.text.toString())
                }
            }
        }

        onUnsavedExitDialog =
            MaterialAlertDialogBuilder(requireContext(), R.style.CustomAlertDialog).apply {
                setTitle(getString(R.string.unsaved_exit_title))
                setMessage(getString(R.string.unsaved_data_loss_message))
                setPositiveButton(getString(R.string.finish)) { _, _ ->
                    findNavController().navigateUp()
                }
                setNegativeButton(getString(R.string.decline)) { _, _ -> }
            }

        activity?.onBackPressedDispatcher?.addCallback(this) {
            if (checkFilledViews()) {
                onUnsavedExitDialog.create().show()
            } else {
                findNavController().navigateUp()
            }
        }
    }

    private fun setupViews(playlist: Playlist) {
        with(binding) {
            this.playlistNameInputText.setText(playlist.name)
            this.playlistDescriptionText.setText(playlist.description)
            this.createPlaylistButton.text = getString(R.string.save_playlist)
            this.selectImageView.background = null
            Glide.with(requireContext())
                .load(playlist.image)
                .placeholder(R.drawable.image_placeholder_512x512)
                .into(this.selectImageView)
        }
    }

    private fun checkFilledViews(): Boolean {
        if (imageUri == null && binding.playlistNameInputText.text.isNullOrBlank() && binding.playlistDescriptionText.text.isNullOrBlank()) {
            return false
        }
        return true
    }

    private fun makeSuccessToast(playlistName: String) {
        Toast.makeText(
            requireContext(),
            getString(R.string.playlist_created_message, playlistName),
            Toast.LENGTH_LONG
        ).show()
    }

    companion object {
        const val PLAYLIST_ID = "playlist_id_"
    }
}