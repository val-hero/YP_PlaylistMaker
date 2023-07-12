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
import com.example.playlistmaker.databinding.FragmentSearchBinding
import com.example.playlistmaker.search.domain.model.Track
import com.example.playlistmaker.search.ui.viewmodel.SearchViewModel
import com.example.playlistmaker.utility.ErrorType
import com.example.playlistmaker.utility.OnClickSupport
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchFragment : Fragment() {
    private lateinit var binding: FragmentSearchBinding

    private val searchAdapter = TrackAdapter()
    private val historyAdapter = TrackAdapter()

    private val viewModel by viewModel<SearchViewModel>()

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

        OnClickSupport.addTo(binding.trackListRecycler).onItemClick { _, position, _ ->
            viewModel.saveToHistory(searchAdapter.tracks[position])
            openPlayer(searchAdapter.tracks[position])
        }

        OnClickSupport.addTo(binding.searchHistoryRecycler).onItemClick { _, position, _ ->
            openPlayer(historyAdapter.tracks[position])
        }

        binding.searchNavigation.setNavigationOnClickListener {
            findNavController().popBackStack()
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

        viewModel.screenState.observe(viewLifecycleOwner) {
            render(it)
        }
    }

    override fun onDetach() {
        super.onDetach()
        OnClickSupport.removeFrom(binding.trackListRecycler)
        OnClickSupport.removeFrom(binding.searchHistoryRecycler)
    }

    private fun openPlayer(track: Track) {
        if (viewModel.trackIsClickable.value == false) return

        viewModel.saveTrack(track)
        viewModel.trackClickDebounce()
        //findNavController().navigate()
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
        val inputMethodManager = requireActivity().
            getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
        inputMethodManager?.hideSoftInputFromWindow(binding.clearSearchField.windowToken, 0)
    }

    private fun clearButtonVisibility(text: String?) {
        binding.clearSearchField.isVisible = !text.isNullOrBlank()
    }
}