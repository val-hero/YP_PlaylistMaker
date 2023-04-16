package com.example.playlistmaker

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView

class TrackAdapter : RecyclerView.Adapter<TrackViewHolder>() {

    private inner class TrackDiffCallback(
        private val oldTracks: ArrayList<Track>,
        private val newTracks: ArrayList<Track>
    ) : DiffUtil.Callback() {
        override fun getOldListSize(): Int = oldTracks.size

        override fun getNewListSize(): Int = newTracks.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
            oldTracks[oldItemPosition].trackId == newTracks[newItemPosition].trackId

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
            oldTracks[oldItemPosition].trackName == newTracks[newItemPosition].trackName
    }

    val tracks = ArrayList<Track>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.track_item, parent, false)
        return TrackViewHolder(view)
    }

    override fun getItemCount(): Int = tracks.size

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(tracks[position])
    }

    fun updateTracks(tracks: ArrayList<Track> = arrayListOf()) {
        val diffCallback = TrackDiffCallback(this.tracks, tracks)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        this.tracks.clear()
        this.tracks.addAll(tracks)
        diffResult.dispatchUpdatesTo(this)
    }
}