package com.example.playlistmaker.search.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.core.domain.model.Track
import com.example.playlistmaker.core.utils.ErrorType
import com.example.playlistmaker.databinding.FragmentSearchBinding
import com.example.playlistmaker.search.ui.viewmodel.SearchViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchFragment : Fragment() {
    private lateinit var binding: FragmentSearchBinding

    private val viewModel by viewModel<SearchViewModel>()

    private val searchAdapter = TrackAdapter(
        onClick = {
            openTrack(it)
            viewModel.saveToHistory(it)
        },
        onLongClick = { true }
    )
    private val historyAdapter = TrackAdapter(
        onClick = { openTrack(it) },
        onLongClick = { true }
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.trackListRecycler.adapter = searchAdapter
        binding.searchHistoryRecycler.adapter = historyAdapter

        binding.searchNavigation.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        binding.searchField.doOnTextChanged { text, _, _, _ ->
            viewModel.onSearchQueryChanged(text.toString())
            clearButtonVisibility(text.toString())
        }

        binding.searchField.setOnFocusChangeListener { _, focused ->
            viewModel.onSearchFieldFocusChanged(focused)
        }

        binding.clearSearchField.setOnClickListener {
            viewModel.onSearchQueryChanged(null)
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

        viewModel.screenState.observe(viewLifecycleOwner) {
            render(it)
        }
    }

    private fun openTrack(track: Track) {
        if (!viewModel.trackIsClickable) return

        viewModel.saveTrack(track)
        viewModel.onTrackClick()
        findNavController().navigate(R.id.action_searchFragment_to_playerFragment)
    }

    private fun render(screenState: SearchScreenState) {
        when (screenState) {
            is SearchScreenState.Content -> showTracks(screenState.tracks)
            is SearchScreenState.Error -> showError(screenState.type)
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

    private fun showError(type: ErrorType) {
        binding.trackListRecycler.isVisible = false
        binding.searchHistoryView.isVisible = false
        binding.progressBar.isVisible = false

        when (type) {
            ErrorType.NOT_FOUND -> binding.notFoundError.isVisible = true
            ErrorType.NO_NETWORK_CONNECTION -> binding.serverError.isVisible = true
            else -> Unit
        }
    }

    private fun hideErrors() {
        binding.notFoundError.isVisible = false
        binding.serverError.isVisible = false
    }

    private fun hideKeyboard() {
        val inputMethodManager =
            requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
        inputMethodManager?.hideSoftInputFromWindow(binding.clearSearchField.windowToken, 0)
    }

    private fun clearButtonVisibility(text: String?) {
        binding.clearSearchField.isVisible = !text.isNullOrBlank()
    }
}