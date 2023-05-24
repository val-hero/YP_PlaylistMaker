package com.example.playlistmaker.presentation.search

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.example.playlistmaker.data.network.ITunesApi
import com.example.playlistmaker.data.network.SearchResponse
import com.example.playlistmaker.data.repository.TrackRepositoryImpl
import com.example.playlistmaker.data.storage.local.SharedPrefsTrackStorage
import com.example.playlistmaker.databinding.ActivitySearchBinding
import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.domain.usecase.search.ClearSearchHistory
import com.example.playlistmaker.domain.usecase.GetTrackList
import com.example.playlistmaker.domain.usecase.SaveTrack
import com.example.playlistmaker.domain.usecase.SaveTrackList
import com.example.playlistmaker.presentation.player.PlayerActivity
import com.example.playlistmaker.presentation.search.adapters.TrackAdapter
import com.example.playlistmaker.utility.JsonConverter
import com.example.playlistmaker.utility.OnClickSupport
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SearchActivity : AppCompatActivity() {
    companion object {
        const val SEARCH_EDIT_TEXT = "search_edit_text"
        const val TRACKS = "tracks"
        const val API_BASE_URL = "https://itunes.apple.com"
        const val SEARCH_DEBOUNCE_DELAY = 2000L
        const val TRACK_CLICK_DELAY = 1000L
    }

    private val retrofit = Retrofit.Builder()
        .baseUrl(API_BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private lateinit var binding: ActivitySearchBinding

    private val iTunesApi = retrofit.create(ITunesApi::class.java)
    private val searchAdapter = TrackAdapter()
    private val historyAdapter = TrackAdapter()
    private var searchText: CharSequence = ""
    private lateinit var handler: Handler
    private val searchRunnable = Runnable { search() }
    private var trackIsClickable = true
    private val searchHistory = SearchHistory()

    private val viewModel: SearchViewModel by viewModels(factoryProducer = {
        SearchViewModelFactory(
            context = applicationContext
        )
    })

    private val trackStorage by lazy { SharedPrefsTrackStorage(applicationContext) }
    private val trackRepository by lazy { TrackRepositoryImpl(trackStorage) }
    private val saveTrackListUseCase by lazy { SaveTrackList(trackRepository) }
    private val getTrackListUseCase by lazy { GetTrackList(trackRepository) }
    private val saveTrackUseCase by lazy { SaveTrack(trackRepository) }
    private val clearSearchHistoryUseCase by lazy { ClearSearchHistory(trackRepository) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        handler = Handler(mainLooper)
        setupTextWatcher()
        binding.trackListRecycler.adapter = searchAdapter
        binding.searchHistoryRecycler.adapter = historyAdapter
        searchHistory.tracks.addAll((getTrackListUseCase()))
        historyAdapter.updateTracks(searchHistory.tracks)

        binding.searchNavigation.setNavigationOnClickListener {
            finish()
        }

        binding.clearButton.setOnClickListener {
            clearSearchRequest()
        }

        binding.refreshButton.setOnClickListener {
            searchDebounce()
        }

        binding.searchField.setOnFocusChangeListener { view, _ ->
            binding.searchHistoryView.isVisible =
                searchHistoryVisibility(view, binding.searchField.text)
        }

        OnClickSupport.addTo(binding.trackListRecycler).onItemClick { _, position, _ ->
            searchHistory.addTrack(searchAdapter.tracks[position])
            historyAdapter.updateTracks(searchHistory.tracks)
            saveTrackListUseCase(historyAdapter.tracks)
            openPlayer(searchAdapter.tracks[position])
        }

        OnClickSupport.addTo(binding.searchHistoryRecycler).onItemClick { _, position, _ ->
            openPlayer(historyAdapter.tracks[position])
        }

        binding.clearHistory.setOnClickListener {
            clearSearchHistoryUseCase()
            searchHistory.tracks.clear()
            historyAdapter.updateTracks()
            binding.searchHistoryView.isVisible = false
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
        binding.searchField.setText(searchText)
        val savedTracks = savedInstanceState.getString(TRACKS, null)
        if (savedTracks != null)
            searchAdapter.updateTracks(JsonConverter.jsonToItemList(savedTracks))
    }

    override fun onDestroy() {
        super.onDestroy()
        OnClickSupport.removeFrom(binding.trackListRecycler)
        OnClickSupport.removeFrom(binding.searchHistoryRecycler)
    }

    private fun openPlayer(track: Track) {
        if (!trackIsClickable) return

        trackIsClickable = false
        val intent = Intent(this, PlayerActivity::class.java)
        saveTrackUseCase(track)
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
                binding.clearButton.isVisible = clearButtonVisibility(text)
                binding.searchHistoryView.isVisible =
                    searchHistoryVisibility(binding.searchField, text)
            }

            override fun afterTextChanged(text: Editable?) {
                searchText = text!!
            }
        }
        binding.searchField.addTextChangedListener(viewModel.setupTextWatcher())
    }

    private fun search() {
        binding.progressBar.isVisible = true
        binding.trackListRecycler.isVisible = false
        iTunesApi.getTracks(searchText.toString()).enqueue(object : Callback<SearchResponse> {
            override fun onResponse(
                call: Call<SearchResponse>,
                response: Response<SearchResponse>
            ) {
                if (response.code() == 200) {
                    binding.progressBar.isVisible = false
                    if (response.body()?.results?.isNotEmpty() == true) {
                        searchAdapter.updateTracks(response.body()!!.results)
                        binding.trackListRecycler.isVisible = true
                    } else {
                        showError(binding.notFoundError)
                    }
                } else {
                    showError(binding.serverError)
                }
            }

            override fun onFailure(call: Call<SearchResponse>, t: Throwable) {
                showError(binding.serverError)
            }
        }
        )
        hideErrors()
    }

    private fun showError(errorView: View) {
        searchAdapter.updateTracks()
        binding.progressBar.isVisible = false
        errorView.isVisible = true
    }

    private fun hideErrors() {
        binding.notFoundError.isVisible = false
        binding.serverError.isVisible = false
    }

    private fun clearSearchRequest() {
        binding.searchField.setText("")
        val inputMethodManager =
            getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
        inputMethodManager?.hideSoftInputFromWindow(binding.clearButton.windowToken, 0)
        binding.searchField.clearFocus()
        searchAdapter.updateTracks()
        handler.removeCallbacks(searchRunnable)
    }

    private fun searchHistoryVisibility(view: View, text: CharSequence?): Boolean =
        historyAdapter.tracks.isNotEmpty() && view.hasFocus() && text.isNullOrEmpty()


    private fun clearButtonVisibility(text: CharSequence?): Boolean = !text.isNullOrEmpty()
}