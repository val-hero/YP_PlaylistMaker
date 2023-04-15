package com.example.playlistmaker

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import java.util.Queue

class TrackAdapter : RecyclerView.Adapter<TrackViewHolder>() {
    companion object {
        const val HISTORY_CAPACITY = 10
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
        this.tracks.clear()
        if (tracks.isNotEmpty()) {
            this.tracks.addAll(tracks)
        }
        notifyDataSetChanged()
    }

    fun addToHistory(track: Track) {
        removeDuplicate(track)
        if (tracks.size < HISTORY_CAPACITY) {
            tracks.add(0, track)
        } else {
            tracks.removeLast()
            notifyItemRemoved(tracks.lastIndex)
            tracks.add(0, track)
        }
        notifyItemChanged(0)
        notifyItemInserted(0)
    }

    private fun removeDuplicate(track: Track) {
        tracks.forEachIndexed { index, comparable ->
            if (track.trackId == comparable.trackId) {
                tracks.removeAt(index)
                notifyItemRemoved(index)
            }
        }
    }
}