package com.example.playlistmaker.presentation

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.Group
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.data.repository.MediaPlayerImpl
import com.example.playlistmaker.data.repository.TrackRepositoryImpl
import com.example.playlistmaker.data.storage.SharedPrefsTrackStorage
import com.example.playlistmaker.domain.models.PlayerState
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.domain.usecase.GetTrack
import com.example.playlistmaker.domain.usecase.PauseTrack
import com.example.playlistmaker.domain.usecase.PlayTrack
import com.example.playlistmaker.domain.usecase.ReleasePlayer
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.floatingactionbutton.FloatingActionButton

class PlayerActivity : AppCompatActivity() {
    private lateinit var trackImage: ImageView
    private lateinit var trackName: TextView
    private lateinit var artistName: TextView
    private lateinit var duration: TextView
    private lateinit var album: TextView
    private lateinit var year: TextView
    private lateinit var genre: TextView
    private lateinit var country: TextView
    private lateinit var playTimer: TextView
    private lateinit var albumViewGroup: Group
    private lateinit var navigation: MaterialToolbar
    private lateinit var playbackToggle: FloatingActionButton
    private lateinit var handler: Handler
    private lateinit var timerRunnable: Runnable

    private val trackStorage by lazy { SharedPrefsTrackStorage(applicationContext) }
    private val trackRepository by lazy { TrackRepositoryImpl(trackStorage) }
    private val getTrackUseCase by lazy { GetTrack(trackRepository) }
    private val mediaPlayer by lazy { MediaPlayerImpl(trackRepository) }
    private val playTrackUseCase by lazy { PlayTrack(mediaPlayer) }
    private val pauseTrackUseCase by lazy { PauseTrack(mediaPlayer) }
    private val releasePlayerUseCase by lazy { ReleasePlayer(mediaPlayer) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)
        initialize()
        fillViews(getTrackUseCase())
        setAlbumVisibility()
        playTimer.text = DEFAULT_TIMER_VALUE
        trackName.isSelected = true // to enable marquee effect

        navigation.setNavigationOnClickListener {
            finish()
        }

        mediaPlayer.setOnStateChangeListener { state ->
            playbackToggle.setOnClickListener {
                playbackControl(state)
            }

            if (state == PlayerState.COMPLETED) {
                handler.removeCallbacks(timerRunnable)
                playTimer.text = DEFAULT_TIMER_VALUE
                setPlayIcon()
            }
        }

        timerRunnable =
            object : Runnable {
                override fun run() {
                    val currentPosition = mediaPlayer.getCurrentPosition()
                    playTimer.text =
                        String.format(MMSS_FORMAT_PATTERN, currentPosition)
                    handler.postDelayed(this, TIMER_UPDATE_DELAY)
                }
            }
    }

    override fun onPause() {
        super.onPause()
        pauseTrackUseCase()
        setPlayIcon()
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(timerRunnable)
        mediaPlayer.removeOnStateChangeListener()
        releasePlayerUseCase()
    }

    private fun initialize() {
        trackImage = findViewById(R.id.player_track_image)
        trackName = findViewById(R.id.player_track_name)
        artistName = findViewById(R.id.player_artist_name)
        duration = findViewById(R.id.duration_value)
        album = findViewById(R.id.album_value)
        year = findViewById(R.id.year_value)
        genre = findViewById(R.id.genre_value)
        country = findViewById(R.id.country_value)
        playTimer = findViewById(R.id.play_timer)
        albumViewGroup = findViewById(R.id.album_group)
        navigation = findViewById(R.id.navigation)
        playbackToggle = findViewById(R.id.play_button)
        handler = Handler(Looper.getMainLooper())
    }

    private fun fillViews(track: Track) {
        trackName.text = track.trackName
        artistName.text = track.artistName
        duration.text = track.formattedDuration()
        album.text = track.collectionName
        year.text = track.releaseYear()
        genre.text = track.genre
        country.text = track.country

        Glide.with(this)
            .load(track.resizedImage())
            .placeholder(R.drawable.player_track_img_placeholder)
            .transform(RoundedCorners(resources.getDimensionPixelSize(R.dimen.player_track_image_corners)))
            .into(trackImage)
    }

    private fun setAlbumVisibility() {
        albumViewGroup.isVisible = album.text.isNotEmpty()
    }


    private fun playbackControl(state: PlayerState) {
        when (state) {
            PlayerState.PLAYING -> {
                pauseTrackUseCase()
                handler.removeCallbacks(timerRunnable)
                setPlayIcon()
            }
            PlayerState.PAUSED, PlayerState.PREPARED, PlayerState.COMPLETED -> {
                playTrackUseCase()
                handler.post(timerRunnable)
                setPauseIcon()
            }
            PlayerState.DEFAULT -> return
        }
    }

    private fun setPauseIcon() {
        playbackToggle.foreground =
            ResourcesCompat.getDrawable(resources, R.drawable.pause_button, null)
    }

    private fun setPlayIcon() {
        playbackToggle.foreground =
            ResourcesCompat.getDrawable(resources, R.drawable.play_button, null)
    }

    private companion object {
        const val TIMER_UPDATE_DELAY = 500L
        const val DEFAULT_TIMER_VALUE = "00:00"
        const val MMSS_FORMAT_PATTERN = "%1\$tM:%1\$tS"
    }
}