package com.example.playlistmaker.utility

// Shared prefs for tracks
const val TRACKS_SHARED_PREFS = "track_shared_prefs"
const val SELECTED_TRACK = "selected_track"
const val TRACK_LIST = "track_list"

//Player Activity related
const val TIMER_UPDATE_DELAY = 500L
const val DEFAULT_TIMER_VALUE = "00:00"
const val MMSS_FORMAT_PATTERN = "%1\$tM:%1\$tS"

// Retrofit
const val ITUNES_API_BASE_URL = "https://itunes.apple.com"

//Search Activity related
const val FOUND_TRACKS = "found_tracks"
const val SEARCH_EDIT_TEXT = "search_edit_text"
const val SEARCH_DEBOUNCE_DELAY = 2000L
const val TRACK_CLICK_DELAY = 1000L
const val HISTORY_CAPACITY = 10