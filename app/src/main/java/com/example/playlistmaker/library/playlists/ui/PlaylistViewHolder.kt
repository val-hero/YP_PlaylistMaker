package com.example.playlistmaker.library.playlists.ui

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.PlaylistItemBinding
import com.example.playlistmaker.library.playlists.domain.model.Playlist

class PlaylistViewHolder(
    private val binding: PlaylistItemBinding,
    private val onClick: (Playlist) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(model: Playlist) {
        binding.playlistItemName.text = model.name
        binding.playlistItemTrackCount.text =
            binding.playlistItemTrackCount.resources.getQuantityString(
                R.plurals.tracks_count,
                model.tracksCount,
                model.tracksCount
            )

        Glide
            .with(binding.root)
            .load(model.image)
            .placeholder(R.drawable.track_image_placeholder)
            .into(binding.playlistItemImage)

        binding.root.setOnClickListener { onClick(model) }
    }
}
