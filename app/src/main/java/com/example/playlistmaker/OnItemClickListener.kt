package com.example.playlistmaker

import android.view.View
import androidx.recyclerview.widget.RecyclerView

interface OnItemClickListener {
    fun onItemClick(recyclerView: RecyclerView, position: Int, view: View)
}