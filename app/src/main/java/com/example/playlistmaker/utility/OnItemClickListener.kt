package com.example.playlistmaker.utility

import android.view.View
import androidx.recyclerview.widget.RecyclerView

fun interface OnItemClickListener {
    fun onItemClick(recyclerView: RecyclerView, position: Int, view: View)
}