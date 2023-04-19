package com.example.playlistmaker

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.Group
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.utility.JsonConverter
import com.google.android.material.appbar.MaterialToolbar
import java.text.SimpleDateFormat
import java.util.Locale

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)
        val currentTrack = JsonConverter.jsonToItem<Track>(intent.extras?.getString("track")!!)
        initialize()
        fillViewsWithTrackInfo(currentTrack)
        setAlbumVisibility()
        trackName.isSelected = true

        navigation.setNavigationOnClickListener {
            finish()
        }
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
    }

    private fun fillViewsWithTrackInfo(track: Track) {
        trackName.text = track.trackName
        artistName.text = track.artistName
        duration.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.duration)
        album.text = track.collectionName
        year.text = SimpleDateFormat("yyyy", Locale.getDefault()).format(track.releaseDate)
        genre.text = track.genre
        country.text = track.country
        playTimer.text = "00:30" //mock

        Glide.with(this)
            .load(track.getResizedImage())
            .placeholder(R.drawable.player_track_img_placeholder)
            .transform(RoundedCorners(resources.getDimensionPixelSize(R.dimen.player_track_image_corners)))
            .into(trackImage)
    }

    private fun setAlbumVisibility() {
        albumViewGroup.isVisible = album.text.isNotEmpty()
    }
}