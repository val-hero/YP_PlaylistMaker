package com.example.playlistmaker.library.playlist_details.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentPlaylistDetailsBinding
import com.example.playlistmaker.library.playlists.domain.model.Playlist
import com.example.playlistmaker.search.ui.TrackAdapter
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistDetailsFragment : Fragment() {
    private lateinit var binding: FragmentPlaylistDetailsBinding
    private val viewModel by viewModel<PlaylistDetailsViewModel>()
    private var bottomSheetBehavior: BottomSheetBehavior<LinearLayout>? = null
    private var alertDialogBuilder: MaterialAlertDialogBuilder? = null

    private val adapter = TrackAdapter(
        onClick = {
            viewModel.saveTrack(it)
            findNavController().navigate(R.id.action_playlistDetailsFragment_to_playerFragment)
        },
        onLongClick = {
            alertDialogBuilder?.show()
            true
        }
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPlaylistDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val playlistId = requireArguments().getLong(PLAYLIST_ID)
        binding.playlistBottomSheetRecycler.adapter = adapter

        bottomSheetBehavior = binding.playlistMenuBottomSheet.let {
            BottomSheetBehavior.from(it).apply {
                state = BottomSheetBehavior.STATE_HIDDEN
            }
        }

        alertDialogBuilder =
            MaterialAlertDialogBuilder(requireContext(), R.style.CustomAlertDialog).apply {
                setTitle(R.string.delete_track_title)
                setPositiveButton(R.string.yes) { _, _ ->

                }
                setNegativeButton(R.string.no) { _, _ ->

                }
            }

        toggleDim()

        viewModel.fetchPlaylistDetails(playlistId)

        viewModel.playlist.observe(viewLifecycleOwner) { playlist ->
            playlist?.let {
                setupPlaylistViews(it)
            }
            viewModel.getTracksInPlaylist(playlist?.tracksIds as List<Long>)
        }

        viewModel.tracksInPlaylist.observe(viewLifecycleOwner) { tracks ->
            adapter.updateTracks(tracks)
        }

        viewModel.playlistDuration.observe(viewLifecycleOwner) { totalMinutes ->
            binding.playlistTotalDuration.text =
                binding.root.resources.getQuantityString(
                    R.plurals.playlist_duration,
                    totalMinutes,
                    totalMinutes
                )
        }
    }

    private fun setupPlaylistViews(playlist: Playlist?) {
        with(binding) {
            this.playlistName.text = playlist?.name
            this.playlistDescription.text = playlist?.description
            this.playlistDescription.isVisible = !playlist?.description.isNullOrBlank()
            Glide.with(requireContext())
                .load(playlist?.image)
                .placeholder(R.drawable.image_placeholder_512x512)
                .into(this.playlistImage)
            this.playlistTracksCount.text = playlist?.tracksCount.toString()
        }
    }

    private fun toggleDim() {
        bottomSheetBehavior?.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> {
                        binding.grayDim.isVisible = false
                    }

                    else -> {
                        binding.grayDim.isVisible = true
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {}
        })
    }

    companion object {
        const val PLAYLIST_ID = "playlist_id"
    }
}