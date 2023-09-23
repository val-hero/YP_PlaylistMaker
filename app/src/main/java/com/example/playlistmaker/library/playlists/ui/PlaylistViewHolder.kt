package com.example.playlistmaker.library.playlists.ui

import android.os.Environment
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.playlistmaker.R
import com.example.playlistmaker.core.utils.PLAYLIST_IMAGES_FOLDER
import com.example.playlistmaker.databinding.PlaylistItemBinding
import com.example.playlistmaker.library.playlists.domain.model.Playlist
import java.io.File

class PlaylistViewHolder(
    private val binding: PlaylistItemBinding,
    private val onClick: (Playlist) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(model: Playlist) {
        binding.playlistItemName.text = model.name
        binding.playlistItemTrackCount.text = "0"

        val filePath = File(
            binding.root.context.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
            PLAYLIST_IMAGES_FOLDER
        )

        Glide
            .with(binding.root)
            .load(model.image?.let { fileName -> File(filePath, fileName) })
            .placeholder(R.drawable.track_image_placeholder)
            .into(binding.playlistItemImage)

        binding.root.setOnClickListener { onClick(model) }
    }
}
