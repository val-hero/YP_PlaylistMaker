package com.example.playlistmaker.player.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityPlayerBinding
import com.example.playlistmaker.player.domain.model.PlayerState
import com.example.playlistmaker.player.ui.viewmodel.PlayerViewModel
import com.example.playlistmaker.search.domain.model.Track
import com.example.playlistmaker.utility.DEFAULT_TIMER_VALUE
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlayerActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPlayerBinding

    private val viewModel by viewModel<PlayerViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        fillViews(viewModel.currentTrack)
        setAlbumVisibility()
        binding.playerTrackName.isSelected = true // to enable marquee effect

        binding.navigation.setNavigationOnClickListener {
            finish()
        }

        binding.playbackButton.setOnClickListener {
            viewModel.playbackControl()
        }

        viewModel.playbackTime.observe(this) {
            binding.playTimer.text = it
        }

        viewModel.playerState.observe(this) { state ->
            setPlaybackButtonIcon(state)
            when (state) {
                PlayerState.PLAYING -> viewModel.runPlaybackTimer()
                PlayerState.PAUSED -> viewModel.pausePlaybackTimer()
                PlayerState.COMPLETED -> {
                    viewModel.pausePlaybackTimer()
                    binding.playTimer.text = DEFAULT_TIMER_VALUE
                }
                else -> return@observe
            }
        }
    }

    override fun onPause() {
        super.onPause()
        viewModel.pausePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.releasePlayer()
    }

    private fun fillViews(track: Track) {
        binding.playerTrackName.text = track.trackName
        binding.playerArtistName.text = track.artistName
        binding.durationValue.text = track.formattedDuration()
        binding.albumValue.text = track.collectionName
        binding.yearValue.text = track.releaseYear()
        binding.genreValue.text = track.genre
        binding.countryValue.text = track.country
        binding.playTimer.text = DEFAULT_TIMER_VALUE

        Glide.with(this).load(track.resizedImage())
            .placeholder(R.drawable.player_track_img_placeholder)
            .transform(RoundedCorners(resources.getDimensionPixelSize(R.dimen.player_track_image_corners)))
            .into(binding.playerTrackImage)
    }

    private fun setAlbumVisibility() {
        binding.albumGroup.isVisible = binding.album.text.isNotEmpty()
    }

    private fun setPlaybackButtonIcon(playerState: PlayerState) {
        if (playerState == PlayerState.PLAYING) {
            binding.playbackButton.foreground =
                ResourcesCompat.getDrawable(resources, R.drawable.pause_button, null)
        } else {
            binding.playbackButton.foreground =
                ResourcesCompat.getDrawable(resources, R.drawable.play_button, null)
        }
    }
}