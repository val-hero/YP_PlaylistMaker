package com.example.playlistmaker

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners

class TrackViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val trackNameView: TextView
    val artistNameView: TextView
    val trackDurationView: TextView
    val trackImageView: ImageView

    init {
        trackNameView = itemView.findViewById(R.id.track_name)
        artistNameView = itemView.findViewById(R.id.artist_name)
        trackDurationView = itemView.findViewById(R.id.track_duration)
        trackImageView = itemView.findViewById(R.id.track_image)
    }

    fun bind(model: Track) {
        trackNameView.text = model.trackName
        artistNameView.text = model.artistName
        trackDurationView.text = model.formattedDuration()

        Glide.with(itemView)
            .load(model.imageUrl)
            .placeholder(R.drawable.track_image_placeholder)
            .transform(RoundedCorners(itemView.resources.getDimensionPixelSize(R.dimen.track_image_corner_radius)))
            .into(trackImageView)
    }
}