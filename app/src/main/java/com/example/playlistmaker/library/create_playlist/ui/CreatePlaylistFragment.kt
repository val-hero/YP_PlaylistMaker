package com.example.playlistmaker.library.create_playlist.ui

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentCreatePlaylistBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class CreatePlaylistFragment : Fragment() {
    private lateinit var binding: FragmentCreatePlaylistBinding
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
            Log.d("FR", checkFilledViews().toString())
            if(checkFilledViews()) {
                onUnsavedExitDialog.show()
            } else {
                findNavController().navigateUp()
            }
        }

        binding.playlistNameInputText.doOnTextChanged { text, start, _, _ ->
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

        onUnsavedExitDialog = MaterialAlertDialogBuilder(requireContext()).apply {
            setTitle(getString(R.string.unsaved_exit_title))
            setMessage(getString(R.string.unsaved_data_loss_message))
            setPositiveButton(getString(R.string.finish)) { _, _ ->
                findNavController().navigateUp()
            }
            setNegativeButton(getString(R.string.decline)) { _, _ -> }
        }

        activity?.onBackPressedDispatcher?.addCallback(this) {
            if(checkFilledViews()) {
                onUnsavedExitDialog.create().show()
            } else {
                findNavController().navigateUp()
            }
        }
    }

    private fun checkFilledViews(): Boolean {
        if(imageUri == null && binding.playlistNameInputText.text.isNullOrBlank() && binding.playlistDescriptionText.text.isNullOrBlank()) {
            return false
        }
        return true
    }
}