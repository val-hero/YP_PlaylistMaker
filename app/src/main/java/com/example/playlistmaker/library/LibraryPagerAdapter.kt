package com.example.playlistmaker.library

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.playlistmaker.library.favourite.ui.FavouriteTracksFragment
import com.example.playlistmaker.library.playlists.ui.PlaylistsFragment

class LibraryPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return if (position == 0) FavouriteTracksFragment.newInstance() else PlaylistsFragment.newInstance()
    }

}