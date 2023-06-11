package com.example.playlistmaker.search.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.example.playlistmaker.databinding.ActivitySearchBinding
import com.example.playlistmaker.search.domain.model.Track
import com.example.playlistmaker.search.ui.viewmodel.SearchViewModel
import com.example.playlistmaker.search.ui.viewmodel.SearchViewModelFactory
import com.example.playlistmaker.player.ui.PlayerActivity
import com.example.playlistmaker.utility.OnClickSupport

class SearchActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySearchBinding

    private val searchAdapter = TrackAdapter()
    private val historyAdapter = TrackAdapter()
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

        binding.searchField.addTextChangedListener(viewModel.setupTextWatcher())

        binding.searchField.setOnFocusChangeListener { _, _ ->
            viewModel.onSearchFieldFocused()
        }

        binding.clearSearchField.setOnClickListener {
            viewModel.clearSearchField()
            val inputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
            inputMethodManager?.hideSoftInputFromWindow(binding.clearSearchField.windowToken, 0)
            binding.searchField.clearFocus()
            binding.searchField.text = null
        }

        binding.refreshButton.setOnClickListener {
            viewModel.runLatestSearch()
        }

        binding.clearHistory.setOnClickListener {
            viewModel.clearSearchHistory()
        }

        viewModel.screenState.observe(this) { state ->
            when(state) {
                is SearchScreenState.Content -> showTracks(state.tracks)
                is SearchScreenState.Error -> showError(state.code)
                is SearchScreenState.History -> showHistory(state.tracks)
                is SearchScreenState.Loading -> showLoading()
                is SearchScreenState.EmptyInput -> {
                    showEmpty()
                }
                is SearchScreenState.HasInput -> {
                    viewModel.searchDebounce(state.input)
                    showClearButton()
                }
            }
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
        //handler.postDelayed({ trackIsClickable = true }, TRACK_CLICK_DELAY)
    }

    private fun showTracks(tracks: ArrayList<Track>) {
        hideErrors()
        binding.progressBar.isVisible = false
        binding.searchHistoryView.isVisible = false

        binding.trackListRecycler.isVisible = true
        searchAdapter.updateTracks(tracks)
    }

    private fun showHistory(tracks: ArrayList<Track>) {
        hideErrors()
        binding.progressBar.isVisible = false
        binding.trackListRecycler.isVisible = false

        historyAdapter.updateTracks(tracks)
        if(tracks.isNotEmpty())
            binding.searchHistoryView.isVisible = true
    }

    private fun showLoading() {
        hideErrors()
        binding.searchHistoryView.isVisible = false
        binding.trackListRecycler.isVisible = false

        binding.progressBar.isVisible = true
    }

    private fun showEmpty() {
        hideErrors()
        binding.searchHistoryView.isVisible = false
        binding.trackListRecycler.isVisible = false
        binding.progressBar.isVisible = false
        binding.clearSearchField.isVisible = false
    }

    private fun showClearButton() {
        binding.clearSearchField.isVisible = true
    }

    private fun showError(errorCode: Int) {
        binding.trackListRecycler.isVisible = false
        binding.searchHistoryView.isVisible = false
        binding.progressBar.isVisible = false

        if (errorCode == 200)
            binding.notFoundError.isVisible = true
        else
            binding.serverError.isVisible = true
    }

    private fun hideErrors() {
        binding.notFoundError.isVisible = false
        binding.serverError.isVisible = false
    }
}