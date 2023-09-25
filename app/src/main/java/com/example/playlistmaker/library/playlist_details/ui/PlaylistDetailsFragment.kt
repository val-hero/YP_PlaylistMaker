package com.example.playlistmaker.library.playlist_details.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentPlaylistDetailsBinding
import com.example.playlistmaker.library.playlist_creation.ui.CreatePlaylistFragment
import com.example.playlistmaker.library.playlists.domain.model.Playlist
import com.example.playlistmaker.search.ui.TrackAdapter
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistDetailsFragment : Fragment() {
    private lateinit var binding: FragmentPlaylistDetailsBinding
    private val viewModel by viewModel<PlaylistDetailsViewModel>()
    private var playlistBottomSheetBehavior: BottomSheetBehavior<LinearLayout>? = null
    private var menuBottomSheetBehavior: BottomSheetBehavior<LinearLayout>? = null
    private var alertDialogBuilder: MaterialAlertDialogBuilder? = null
    private var selectedTrackId: Long? = null
    private var playlistId: Long? = null
    private var shareMessage: String? = null

    private val adapter = TrackAdapter(
        onClick = {
            viewModel.saveTrack(it)
            findNavController().navigate(R.id.action_playlistDetailsFragment_to_playerFragment)
        },
        onLongClick = {
            selectedTrackId = it.id
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
        playlistId = requireArguments().getLong(PLAYLIST_ID)
        viewModel.fetchPlaylistDetails(playlistId!!)

        binding.playlistBottomSheetRecycler.adapter = adapter

        menuBottomSheetBehavior = BottomSheetBehavior.from(binding.playlistMenuBottomSheet).apply {
            this?.state = BottomSheetBehavior.STATE_HIDDEN
        }

        playlistBottomSheetBehavior = BottomSheetBehavior.from(binding.playlistBottomSheet)

        setupBottomSheetCallbacks()
        setupMenuOnClickListeners()

        binding.playlistMenuIcon.setOnClickListener {
            playlistBottomSheetBehavior.apply {
                this?.isHideable = true
                this?.state = BottomSheetBehavior.STATE_HIDDEN
            }
            menuBottomSheetBehavior.apply {
                this?.state = BottomSheetBehavior.STATE_HALF_EXPANDED
            }
        }

        binding.playlistDetailsToolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }


        alertDialogBuilder =
            MaterialAlertDialogBuilder(requireContext(), R.style.CustomAlertDialog).apply {
                setTitle(R.string.delete_track_title)
                setPositiveButton(R.string.yes) { _, _ ->
                    selectedTrackId?.let { viewModel.deleteFromPlaylist(it) }
                }
                setNegativeButton(R.string.no) { _, _ -> }
            }

        binding.playlistShareIcon.setOnClickListener {
            sharePlaylist()
        }

        viewModel.playlist.observe(viewLifecycleOwner) { playlist ->
            playlist?.let {
                setupPlaylistViews(it)
                setupMenuViews(it)
            }
            viewModel.getTracksInPlaylist(playlist?.tracksIds ?: arrayListOf())
        }

        viewModel.tracksInPlaylist.observe(viewLifecycleOwner) { tracks ->
            adapter.updateTracks(tracks)
            viewModel.createPlaylistInfoMessage(
                resources.getQuantityString(
                    R.plurals.tracks_count,
                    adapter.tracks.count(),
                    adapter.tracks.count()
                )
            )

            if (adapter.tracks.isEmpty()) {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.empty_playlist),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        viewModel.playlistDuration.observe(viewLifecycleOwner) { totalMinutes ->
            binding.playlistTotalDuration.text =
                binding.root.resources.getQuantityString(
                    R.plurals.playlist_duration,
                    totalMinutes,
                    totalMinutes
                )
        }

        viewModel.playlistInfoMessage.observe(viewLifecycleOwner) {
            shareMessage = it
        }
    }

    private fun setupPlaylistViews(playlist: Playlist?) {
        with(binding) {
            this.playlistName.text = playlist?.name
            this.playlistDescription.text = playlist?.description
            this.playlistDescription.isVisible = !playlist?.description.isNullOrBlank()
            this.playlistTracksCount.text = playlist?.tracksCount.toString()
            Glide.with(requireContext())
                .load(playlist?.image)
                .placeholder(R.drawable.image_placeholder_512x512)
                .into(this.playlistImage)
        }
    }

    private fun setupMenuViews(playlist: Playlist?) {
        with(binding) {
            this.playlistBottomSheetName.text = playlist?.name
            this.playlistBottomSheetTracksCount.text =
                resources.getQuantityString(
                    R.plurals.tracks_count,
                    playlist!!.tracksCount,
                    playlist.tracksCount
                )
            Glide.with(requireContext())
                .load(playlist?.image)
                .placeholder(R.drawable.image_placeholder_512x512)
                .into(this.playlistBottomSheetImage)
        }
    }

    private fun createShareIntent(message: String) {
        Intent.createChooser(
            Intent().apply {
                action = Intent.ACTION_SEND
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, message)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                requireContext().startActivity(this)
            },
            null
        )
    }

    private fun setupMenuOnClickListeners() {
        binding.playlistShareButton.setOnClickListener {
            sharePlaylist()
        }
        binding.playlistDeleteButton.setOnClickListener {
            showDeleteDialog()
        }

        binding.playlistEditButton.setOnClickListener {
            findNavController().navigate(
                R.id.action_playlistDetailsFragment_to_createPlaylistFragment,
                bundleOf(CreatePlaylistFragment.PLAYLIST_ID to playlistId)
            )
        }
    }

    private fun sharePlaylist() {
        if (adapter.tracks.isEmpty()) {
            Toast.makeText(
                requireContext(),
                getString(R.string.playlist_is_empty_message),
                Toast.LENGTH_SHORT
            ).show()
        } else {
            shareMessage?.let { msg -> createShareIntent(msg) }
            menuBottomSheetBehavior?.state = BottomSheetBehavior.STATE_HIDDEN
        }
    }

    private fun showDeleteDialog() {
        MaterialAlertDialogBuilder(requireContext(), R.style.CustomAlertDialog).apply {
            setTitle(getString(R.string.playlist_delete_confirm_title, binding.playlistName.text))
            setPositiveButton(R.string.yes) { _, _ ->
                viewModel.deletePlaylist {
                    findNavController().popBackStack()
                }
            }
            setNegativeButton(R.string.no) { _, _ -> }
        }.show()
    }

    private fun setupBottomSheetCallbacks() {
        menuBottomSheetBehavior?.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> {
                        binding.grayDim.isVisible = false
                        playlistBottomSheetBehavior?.state = BottomSheetBehavior.STATE_COLLAPSED
                    }

                    else -> {
                        binding.grayDim.isVisible = true
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
            }
        })

        playlistBottomSheetBehavior?.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_COLLAPSED -> {
                        playlistBottomSheetBehavior?.isHideable = false
                        bottomSheet.isVisible = true
                    }

                    BottomSheetBehavior.STATE_HIDDEN -> {
                        bottomSheet.isVisible = false
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
            }
        })
    }

    companion object {
        const val PLAYLIST_ID = "playlist_id"
    }
}