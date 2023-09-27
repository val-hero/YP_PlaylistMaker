package com.example.playlistmaker.player.ui

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.PlaylistItemLinearBinding
import com.example.playlistmaker.library.playlists.domain.model.Playlist

class PlayerBottomSheetViewHolder(
    private val binding: PlaylistItemLinearBinding,
    private val onClick: (Playlist) -> Unit
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(model: Playlist) {
        binding.linearPlaylistName.text = model.name
        binding.linearPlaylistTracksCount.text =
            binding.linearPlaylistTracksCount.resources.getQuantityString(
                R.plurals.tracks_count,
                model.tracksCount,
                model.tracksCount
            )

        Glide
            .with(binding.root)
            .load(model.image)
            .placeholder(R.drawable.track_image_placeholder)
            .into(binding.linearPlaylistImage)

        binding.root.setOnClickListener { onClick(model) }
    }
}