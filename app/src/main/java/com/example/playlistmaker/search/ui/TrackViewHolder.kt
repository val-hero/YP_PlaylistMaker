package com.example.playlistmaker.search.ui

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.core.model.Track
import com.example.playlistmaker.core.utils.asMinutesAndSeconds
import com.example.playlistmaker.databinding.TrackItemBinding

class TrackViewHolder(
    private val binding: TrackItemBinding,
    private val onClick: (Track) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(model: Track) {
        binding.trackName.text = model.trackName
        binding.artistName.text = model.artistName
        binding.trackDuration.text = model.duration.asMinutesAndSeconds()
        binding.root.setOnClickListener { onClick(model) }

        Glide.with(itemView)
            .load(model.imageUrl)
            .placeholder(R.drawable.track_image_placeholder)
            .transform(RoundedCorners(itemView.resources.getDimensionPixelSize(R.dimen.track_image_corner_radius)))
            .into(binding.trackImage)
    }
}