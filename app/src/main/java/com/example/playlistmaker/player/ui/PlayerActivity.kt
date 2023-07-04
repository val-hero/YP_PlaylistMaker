package com.example.playlistmaker.player.ui

import android.os.Bundle
import android.widget.Toast
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
import com.example.playlistmaker.utility.ErrorType
import com.example.playlistmaker.utility.asMinutesAndSeconds
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlayerActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPlayerBinding

    private val viewModel: PlayerViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.navigation.setNavigationOnClickListener {
            finish()
        }

        binding.playbackButton.setOnClickListener {
            viewModel.playbackControl()
        }

        viewModel.playerState.observe(this) { state ->
            setPlaybackButtonIcon(state)
            when (state) {
                is PlayerState.Prepared -> {
                    setupViews(state.track)
                }

                is PlayerState.Default, PlayerState.Completed -> {
                    binding.playTimer.text = DEFAULT_TIMER_VALUE
                }

                is PlayerState.Playing -> {
                    binding.playTimer.text = state.playbackTime.asMinutesAndSeconds()
                }

                is PlayerState.Error -> {
                    Toast.makeText(this, getErrorMessage(state.errorType), Toast.LENGTH_SHORT).show()
                }

                else -> Unit
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

    private fun setupViews(track: Track) {
        with(binding) {
            playerTrackName.text = track.trackName
            playerArtistName.text = track.artistName
            durationValue.text = track.formattedDuration()
            albumValue.text = track.collectionName
            yearValue.text = track.releaseYear()
            genreValue.text = track.genre
            countryValue.text = track.country
            albumGroup.isVisible = albumValue.text.isNotEmpty()
            playerTrackName.isSelected = true

            Glide.with(this@PlayerActivity).load(track.resizedImage())
                .placeholder(R.drawable.player_track_img_placeholder)
                .transform(RoundedCorners(resources.getDimensionPixelSize(R.dimen.player_track_image_corners)))
                .into(playerTrackImage)
        }
    }

    private fun setPlaybackButtonIcon(state: PlayerState) {
        binding.playbackButton.foreground =
            if (state is PlayerState.Playing)
                ResourcesCompat.getDrawable(resources, R.drawable.pause_button, null)
            else
                ResourcesCompat.getDrawable(resources, R.drawable.play_button, null)
    }

    private fun getErrorMessage(errorType: ErrorType): String = when (errorType) {
        ErrorType.FAILED_TO_LOAD -> getString(R.string.track_prepare_fail_error)
        ErrorType.ACTION_CANT_BE_PERFORMED -> getString(R.string.cant_be_played_error)
        else -> getString(R.string.unknown_error)
    }

    companion object {
        const val DEFAULT_TIMER_VALUE = "00:00"
    }
}