package com.example.playlistmaker.search.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.core.domain.model.Track
import com.example.playlistmaker.databinding.TrackItemBinding

class TrackAdapter(private val onClick: (Track) -> Unit) : RecyclerView.Adapter<TrackViewHolder>() {
    val tracks = ArrayList<Track>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val layoutInspector = LayoutInflater.from(parent.context)
        return TrackViewHolder(TrackItemBinding.inflate(layoutInspector, parent, false), onClick)
    }

    override fun getItemCount(): Int = tracks.size

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(tracks[position])
    }

    fun updateTracks(newTracks: List<Track> = listOf()) {
        val oldTracks = this.tracks
        val diffResult = DiffUtil.calculateDiff(object : DiffUtil.Callback() {
            override fun getOldListSize(): Int = oldTracks.size

            override fun getNewListSize(): Int = newTracks.size

            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
                oldTracks[oldItemPosition].id == newTracks[newItemPosition].id

            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
                oldTracks[oldItemPosition].trackName == newTracks[newItemPosition].trackName
        })
        this.tracks.clear()
        this.tracks.addAll(newTracks)
        diffResult.dispatchUpdatesTo(this)
    }
}