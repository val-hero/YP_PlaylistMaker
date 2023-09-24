package com.example.playlistmaker.library.create_playlist.ui

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentCreatePlaylistBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.koin.androidx.viewmodel.ext.android.viewModel

class CreatePlaylistFragment : Fragment() {
    private lateinit var binding: FragmentCreatePlaylistBinding
    private val viewModel by viewModel<CreatePlaylistViewModel>()
    private lateinit var onUnsavedExitDialog: MaterialAlertDialogBuilder
    private var imageUri: Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCreatePlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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
            viewModel.createPlaylist(
                name = binding.playlistNameInputText.text.toString(),
                description = binding.playlistDescriptionText.text.toString(),
                image = imageUri.toString()
            ) {
                findNavController().navigateUp()
                makeSuccessToast(binding.playlistNameInputText.text.toString())
            }
        }

        onUnsavedExitDialog = MaterialAlertDialogBuilder(requireContext(), R.style.CustomAlertDialog).apply {
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
}