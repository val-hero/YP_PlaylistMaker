package com.example.playlistmaker

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.utility.JsonConverter
import com.example.playlistmaker.utility.OnClickSupport
import com.example.playlistmaker.utility.SharedPrefsEditor
import com.google.android.material.appbar.MaterialToolbar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SearchActivity : AppCompatActivity() {
    companion object {
        const val SEARCH_EDIT_TEXT = "search_edit_text"
        const val SEARCH_HISTORY_PREFS = "search_history"
        const val TRACKS = "tracks"
        const val API_BASE_URL = "https://itunes.apple.com"
        const val SELECTED_TRACK = "selected_track"
        const val SEARCH_DEBOUNCE_DELAY = 2000L
        const val TRACK_CLICK_DELAY = 1000L
    }

    private val retrofit = Retrofit.Builder()
        .baseUrl(API_BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    //region Variables
    private val iTunesApi = retrofit.create(ITunesApi::class.java)
    private val searchAdapter = TrackAdapter()
    private val historyAdapter = TrackAdapter()
    private var searchText: CharSequence = ""
    private lateinit var searchEditText: EditText
    private lateinit var clearButton: ImageView
    private lateinit var backButton: MaterialToolbar
    private lateinit var refreshButton: Button
    private lateinit var notFoundError: LinearLayout
    private lateinit var serverError: LinearLayout
    private lateinit var tracksRecycler: RecyclerView
    private lateinit var searchHistoryRecycler: RecyclerView
    private lateinit var sharedPrefsEditor: SharedPrefsEditor
    private lateinit var searchHistoryView: LinearLayout
    private lateinit var clearHistoryButton: Button
    private lateinit var searchHistory: SearchHistory
    private lateinit var progressBar: ProgressBar
    private lateinit var handler: Handler
    private val searchRunnable = Runnable { search() }
    private var trackIsClickable = true
    //endregion

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        initialize()
        setupTextWatcher()
        tracksRecycler.adapter = searchAdapter
        searchHistoryRecycler.adapter = historyAdapter
        searchHistory.tracks.addAll(sharedPrefsEditor.getItems(TRACKS))
        historyAdapter.updateTracks(searchHistory.tracks)

        backButton.setNavigationOnClickListener {
            finish()
        }

        clearButton.setOnClickListener {
            clearSearchRequest()
        }

        refreshButton.setOnClickListener {
            search()
        }

        searchEditText.setOnFocusChangeListener { view, _ ->
            searchHistoryView.isVisible = searchHistoryVisibility(view, searchEditText.text)
        }

        OnClickSupport.addTo(tracksRecycler).onItemClick { _, position, _ ->
            searchHistory.addTrack(searchAdapter.tracks[position])
            historyAdapter.updateTracks(searchHistory.tracks)
            sharedPrefsEditor.addItemList(TRACKS, historyAdapter.tracks)
            openTrack(searchAdapter.tracks[position])
        }

        OnClickSupport.addTo(searchHistoryRecycler).onItemClick { _, position, _ ->
            openTrack(historyAdapter.tracks[position])
        }

        clearHistoryButton.setOnClickListener {
            sharedPrefsEditor.clear()
            searchHistory.tracks.clear()
            historyAdapter.updateTracks()
            searchHistoryView.isVisible = false
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putCharSequence(SEARCH_EDIT_TEXT, searchText)
        outState.putString(TRACKS, JsonConverter.itemListToJson(searchAdapter.tracks))
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        searchText = savedInstanceState.getCharSequence(SEARCH_EDIT_TEXT, "")
        searchEditText.setText(searchText)
        val savedTracks = savedInstanceState.getString(TRACKS, null)
        if (savedTracks != null)
            searchAdapter.updateTracks(JsonConverter.jsonToItemList(savedTracks))
    }

    override fun onDestroy() {
        super.onDestroy()
        OnClickSupport.removeFrom(tracksRecycler)
        OnClickSupport.removeFrom(searchHistoryRecycler)
    }

    private fun initialize() {
        searchEditText = findViewById(R.id.search_field)
        clearButton = findViewById(R.id.clear_button)
        backButton = findViewById(R.id.search_back_button)
        refreshButton = findViewById(R.id.refresh_button)
        notFoundError = findViewById(R.id.not_found_error)
        serverError = findViewById(R.id.server_error)
        tracksRecycler = findViewById(R.id.track_list_recycler)
        searchHistoryRecycler = findViewById(R.id.search_history_recycler)
        searchHistoryView = findViewById(R.id.search_history)
        clearHistoryButton = findViewById(R.id.clear_history)
        progressBar = findViewById(R.id.progress_bar)
        searchHistory = SearchHistory()
        val sharedPrefs = getSharedPreferences(SEARCH_HISTORY_PREFS, MODE_PRIVATE)
        sharedPrefsEditor = SharedPrefsEditor(sharedPrefs)
        handler = Handler(Looper.getMainLooper())
    }

    private fun openTrack(track: Track) {
        if (!trackIsClickable) return

        trackIsClickable = false
        val intent = Intent(this, PlayerActivity::class.java)
        intent.putExtra(SELECTED_TRACK, JsonConverter.itemToJson(track))
        startActivity(intent)
        handler.postDelayed({ trackIsClickable = true }, TRACK_CLICK_DELAY)
    }

    private fun searchDebounce() {
        handler.removeCallbacks(searchRunnable)
        handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)
    }

    private fun setupTextWatcher() {
        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (!text.isNullOrEmpty()) {
                    searchDebounce()
                }
                if (text.isNullOrEmpty()) {
                    hideErrors()

                    handler.removeCallbacks(searchRunnable)
                }
                clearButton.isVisible = clearButtonVisibility(text)
                searchHistoryView.isVisible = searchHistoryVisibility(searchEditText, text)
            }

            override fun afterTextChanged(text: Editable?) {
                searchText = text!!
            }
        }
        searchEditText.addTextChangedListener(textWatcher)
    }

    private fun search() {
        progressBar.isVisible = true
        tracksRecycler.isVisible = false
        iTunesApi.getTracks(searchText.toString()).enqueue(object : Callback<SearchResponse> {
            override fun onResponse(
                call: Call<SearchResponse>,
                response: Response<SearchResponse>
            ) {
                if (response.code() == 200) {
                    progressBar.isVisible = false
                    if (response.body()?.results?.isNotEmpty() == true) {
                        searchAdapter.updateTracks(response.body()!!.results)
                        tracksRecycler.isVisible = true
                    } else {
                        showError(notFoundError)
                    }
                } else {
                    showError(serverError)
                }
            }

            override fun onFailure(call: Call<SearchResponse>, t: Throwable) {
                showError(serverError)
            }
        }
        )
        hideErrors()
    }

    private fun showError(errorView: View) {
        searchAdapter.updateTracks()
        progressBar.isVisible = false
        errorView.isVisible = true
    }

    private fun hideErrors() {
        notFoundError.isVisible = false
        serverError.isVisible = false
    }

    private fun clearSearchRequest() {
        searchEditText.setText("")
        val inputMethodManager =
            getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
        inputMethodManager?.hideSoftInputFromWindow(clearButton.windowToken, 0)
        searchEditText.clearFocus()
        searchAdapter.updateTracks()
        handler.removeCallbacks(searchRunnable)
    }

    private fun searchHistoryVisibility(view: View, text: CharSequence?): Boolean =
        historyAdapter.tracks.isNotEmpty() && view.hasFocus() && text.isNullOrEmpty()


    private fun clearButtonVisibility(text: CharSequence?): Boolean = !text.isNullOrEmpty()
}