package com.example.playlistmaker.search.ui

import android.content.Intent
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import com.example.playlistmaker.databinding.ActivitySearchBinding
import com.example.playlistmaker.player.ui.PlayerActivity
import com.example.playlistmaker.search.domain.model.Track
import com.example.playlistmaker.search.ui.viewmodel.SearchViewModel
import com.example.playlistmaker.utility.OnClickSupport
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySearchBinding

    private val searchAdapter = TrackAdapter()
    private val historyAdapter = TrackAdapter()

    private val viewModel by viewModel<SearchViewModel>()

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

        binding.searchField.doOnTextChanged { text, _, _, _ ->
            viewModel.onSearchTextChanged(text.toString())
            clearButtonVisibility(text.toString())
        }

        binding.searchField.setOnFocusChangeListener { _, focused ->
            viewModel.onSearchFieldFocusChanged(focused)
        }

        binding.clearSearchField.setOnClickListener {
            viewModel.clearSearchField()
            hideKeyboard()
            binding.searchField.clearFocus()
            binding.searchField.text = null
        }

        binding.refreshButton.setOnClickListener {
            viewModel.runLatestSearch()
        }

        binding.clearHistory.setOnClickListener {
            viewModel.clearSearchHistory()
        }

        viewModel.screenState.observe(this) {
            render(it)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        OnClickSupport.removeFrom(binding.trackListRecycler)
        OnClickSupport.removeFrom(binding.searchHistoryRecycler)
    }

    private fun openPlayer(track: Track) {
        if (viewModel.trackIsClickable.value == false) return

        viewModel.saveTrack(track)
        viewModel.trackClickDebounce()
        startActivity(Intent(this, PlayerActivity::class.java))
    }

    private fun render(screenState: SearchScreenState) {
        when (screenState) {
            is SearchScreenState.Content -> showTracks(screenState.tracks)
            is SearchScreenState.Error -> showError(screenState.code)
            is SearchScreenState.History -> showHistory(screenState.tracks)
            is SearchScreenState.Loading -> showLoading()
            is SearchScreenState.Empty -> showEmpty()
        }
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
        binding.clearSearchField.isVisible = false

        historyAdapter.updateTracks(tracks)
        if (tracks.isNotEmpty())
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
    }

    private fun showError(code: Int) {
        binding.trackListRecycler.isVisible = false
        binding.searchHistoryView.isVisible = false
        binding.progressBar.isVisible = false

        if (code == 200)
            binding.notFoundError.isVisible = true
        else
            binding.serverError.isVisible = true
    }

    private fun hideErrors() {
        binding.notFoundError.isVisible = false
        binding.serverError.isVisible = false
    }

    private fun hideKeyboard() {
        val inputMethodManager =
            getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager?
        inputMethodManager?.hideSoftInputFromWindow(binding.clearSearchField.windowToken, 0)
    }

    private fun clearButtonVisibility(text: String?) {
        binding.clearSearchField.isVisible = !text.isNullOrBlank()
    }
}