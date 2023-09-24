package com.example.playlistmaker.player.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.databinding.PlaylistItemLinearBinding
import com.example.playlistmaker.library.playlists.domain.model.Playlist

class PlayerBottomSheetAdapter(
    private val onClick: (Playlist) -> Unit,
) : RecyclerView.Adapter<PlayerBottomSheetViewHolder>() {
    private val playlists = arrayListOf<Playlist>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayerBottomSheetViewHolder {
        val layoutInspector = LayoutInflater.from(parent.context)
        return PlayerBottomSheetViewHolder(
            PlaylistItemLinearBinding.inflate(layoutInspector, parent, false),
            onClick
        )
    }

    override fun getItemCount(): Int = playlists.size

    override fun onBindViewHolder(holder: PlayerBottomSheetViewHolder, position: Int) {
        holder.bind(playlists[position])
    }

    fun updatePlaylists(newPlaylists: List<Playlist> = listOf()) {
        val oldPlaylists = this.playlists
        val diffResult = DiffUtil.calculateDiff(object : DiffUtil.Callback() {
            override fun getOldListSize(): Int = oldPlaylists.size

            override fun getNewListSize(): Int = newPlaylists.size

            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
                oldPlaylists[oldItemPosition].id == newPlaylists[newItemPosition].id

            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
                oldPlaylists[oldItemPosition].name == newPlaylists[newItemPosition].name
        })
        this.playlists.clear()
        this.playlists.addAll(newPlaylists)
        diffResult.dispatchUpdatesTo(this)
    }
}