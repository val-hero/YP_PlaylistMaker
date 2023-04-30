package com.example.playlistmaker

import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.Group
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.utility.JsonConverter
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.floatingactionbutton.FloatingActionButton

enum class PlayerState {
    DEFAULT,
    PREPARED,
    PLAYING,
    PAUSED
}

class PlayerActivity : AppCompatActivity() {
    private companion object {
        const val TIMER_UPDATE_DELAY = 500L
        const val DEFAULT_TIMER_VALUE = "00:00"
        const val MMSS_FORMAT_PATTERN = "%1\$tM:%1\$tS"
    }

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
    private lateinit var currentTrack: Track
    private lateinit var handler: Handler
    private lateinit var timerRunnable: Runnable

    private val mediaPlayer = MediaPlayer()
    private var playerState = PlayerState.DEFAULT

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)
        initialize()
        currentTrack =
            JsonConverter.jsonToItem(intent.extras?.getString(SearchActivity.SELECTED_TRACK)!!)
        fillViews(currentTrack)
        setAlbumVisibility()
        trackName.isSelected = true // to enable marquee effect

        preparePlayer()
        playbackToggle.setOnClickListener {
            playbackControl()
            setPlaybackToggleIcon()
        }

        navigation.setNavigationOnClickListener {
            finish()
        }

        timerRunnable = object : Runnable {
            override fun run() {
                if (mediaPlayer.isPlaying) {
                    val currentPosition = mediaPlayer.currentPosition.toLong()
                    playTimer.text =
                        String.format(MMSS_FORMAT_PATTERN, currentPosition)
                    handler.postDelayed(this, TIMER_UPDATE_DELAY)
                }
            }
        }

        mediaPlayer.setOnCompletionListener {
            playTimer.text = DEFAULT_TIMER_VALUE
            playerState = PlayerState.PREPARED
            handler.removeCallbacks(timerRunnable)
            setPlaybackToggleIcon()
        }
    }

    override fun onPause() {
        super.onPause()
        pausePlayer()
        setPlaybackToggleIcon()
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(timerRunnable)
        mediaPlayer.release()
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

    private fun preparePlayer() {
        mediaPlayer.apply {
            setDataSource(currentTrack.previewUrl)
            prepareAsync()
            setOnPreparedListener {
                playerState = PlayerState.PREPARED
            }
        }
    }

    private fun startPlayer() {
        mediaPlayer.start()
        playerState = PlayerState.PLAYING
        handler.post(timerRunnable)
    }

    private fun pausePlayer() {
        mediaPlayer.pause()
        playerState = PlayerState.PAUSED
        handler.removeCallbacks(timerRunnable)
    }

    private fun playbackControl() {
        when (playerState) {
            PlayerState.PLAYING -> pausePlayer()
            PlayerState.PAUSED, PlayerState.PREPARED -> startPlayer()
            PlayerState.DEFAULT -> return
        }
    }

    private fun setPlaybackToggleIcon() = when (playerState) {
        PlayerState.PLAYING -> playbackToggle.foreground =
            ResourcesCompat.getDrawable(resources, R.drawable.pause_button, null)
        else -> playbackToggle.foreground =
            ResourcesCompat.getDrawable(resources, R.drawable.play_button, null)
    }
}