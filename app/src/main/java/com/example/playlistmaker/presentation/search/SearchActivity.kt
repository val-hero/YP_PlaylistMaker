package com.example.playlistmaker.presentation.search

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.example.playlistmaker.data.repository.TrackRepositoryImpl
import com.example.playlistmaker.data.storage.local.SharedPrefsTrackStorage
import com.example.playlistmaker.databinding.ActivitySearchBinding
import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.domain.usecase.GetTrackList
import com.example.playlistmaker.domain.usecase.SaveTrack
import com.example.playlistmaker.domain.usecase.SaveTrackList
import com.example.playlistmaker.domain.usecase.search.ClearSearchHistory
import com.example.playlistmaker.presentation.player.PlayerActivity
import com.example.playlistmaker.presentation.search.adapters.TrackAdapter
import com.example.playlistmaker.utility.*

class SearchActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySearchBinding

    private val searchAdapter = TrackAdapter()
    private val historyAdapter = TrackAdapter()
    private lateinit var handler: Handler
    private val searchRunnable by lazy { Runnable { viewModel.search() } }
    private var trackIsClickable = true

    private val viewModel: SearchViewModel by viewModels(factoryProducer = {
        SearchViewModelFactory(
            context = applicationContext
        )
    })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        handler = Handler(mainLooper)
        binding.trackListRecycler.adapter = searchAdapter
        binding.searchHistoryRecycler.adapter = historyAdapter

        OnClickSupport.addTo(binding.trackListRecycler).onItemClick { _, position, _ ->
            viewModel.saveToHistory(searchAdapter.tracks[position])
            openPlayer(searchAdapter.tracks[position])
        }

        OnClickSupport.addTo(binding.searchHistoryRecycler).onItemClick { _, position, _ ->
            openPlayer(historyAdapter.tracks[position])
        }

        binding.searchNavigation.setNavigationOnClickListener {
            finish()
        }

        binding.clearButton.setOnClickListener {
            clearSearchRequest()
        }

        binding.refreshButton.setOnClickListener {
            searchDebounce()
        }

        binding.searchField.addTextChangedListener(viewModel.setupTextWatcher())

        binding.searchField.setOnFocusChangeListener { view, _ ->
            binding.searchHistoryView.isVisible =
                searchHistoryVisibility(view, binding.searchField.text)
        }

        binding.clearHistory.setOnClickListener {
            viewModel.clearSearchHistory()
            binding.searchHistoryView.isVisible = false
        }

        viewModel.searchHistory.observe(this) { history ->
            history?.let { historyAdapter.updateTracks(it) }
        }

        viewModel.progressBarVisibility.observe(this) { visibility ->
            binding.progressBar.isVisible = visibility
            binding.trackListRecycler.isVisible = !visibility
        }

        viewModel.searchInput.observe(this) { input ->
            if(input.isNotBlank()) {
                searchDebounce()
            }
            binding.clearButton.isVisible = clearButtonVisibility(input)
            binding.searchHistoryView.isVisible =
                searchHistoryVisibility(binding.searchField, input)
        }

        viewModel.searchResult.observe(this) { result ->
            result?.let {
                when (result) {
                    is Result.Success -> searchAdapter.updateTracks(result.data)
                    is Result.Error -> showError(binding.serverError)
                    is Result.NotFound -> showError(binding.notFoundError)
                }
            } ?: searchAdapter.updateTracks()
        }
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
        viewModel.saveTrack(track)
        startActivity(intent)
        handler.postDelayed({ trackIsClickable = true }, TRACK_CLICK_DELAY)
    }

    private fun searchDebounce() {
        hideErrors()
        handler.removeCallbacks(searchRunnable)
        handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)
    }

    private fun showError(errorView: View) {
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
        hideErrors()
        handler.removeCallbacks(searchRunnable)
    }

    private fun searchHistoryVisibility(view: View, text: CharSequence?): Boolean =
        historyAdapter.tracks.isNotEmpty() && view.hasFocus() && text.isNullOrEmpty()


    private fun clearButtonVisibility(text: CharSequence?): Boolean = !text.isNullOrEmpty()
}