package com.example.playlistmaker.presentation.player

import android.os.Bundle
import android.os.Handler
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityPlayerBinding
import com.example.playlistmaker.domain.models.PlayerState
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.utility.DEFAULT_TIMER_VALUE
import com.example.playlistmaker.utility.TIMER_UPDATE_DELAY

class PlayerActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPlayerBinding
    private lateinit var handler: Handler
    private lateinit var timerRunnable: Runnable

    private val viewModel: PlayerViewModel by viewModels(
        factoryProducer = { PlayerViewModelFactory(applicationContext) })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        handler = Handler(mainLooper)

        fillViews(viewModel.currentTrack)
        setAlbumVisibility()
        binding.playerTrackName.isSelected = true // to enable marquee effect

        binding.navigation.setNavigationOnClickListener {
            finish()
        }

        binding.playbackButton.setOnClickListener {
            viewModel.playbackControl()
        }

        viewModel.playbackTime.observe(this) { time ->
            binding.playTimer.text = time
        }

        viewModel.playerState.observe(this) { state ->
            setPlaybackButtonIcon(state)
            when (state) {
                PlayerState.PLAYING -> handler.post(timerRunnable)
                PlayerState.PAUSED -> handler.removeCallbacks(timerRunnable)
                PlayerState.COMPLETED -> {
                    handler.removeCallbacks(timerRunnable)
                    binding.playTimer.text = DEFAULT_TIMER_VALUE
                }
                else -> return@observe
            }
        }

        timerRunnable =
            object : Runnable {
                override fun run() {
                    viewModel.updatePlaybackTime()
                    handler.postDelayed(this, TIMER_UPDATE_DELAY)
                }

            }
    }

    override fun onPause() {
        super.onPause()
        viewModel.pausePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(timerRunnable)
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

        Glide.with(this)
            .load(track.resizedImage())
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