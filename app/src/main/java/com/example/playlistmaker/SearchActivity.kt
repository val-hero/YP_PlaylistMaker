package com.example.playlistmaker

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.utility.JsonConverter
import com.example.playlistmaker.utility.OnClickSupport
import com.example.playlistmaker.utility.OnItemClickListener
import com.example.playlistmaker.utility.SharedPrefsEditor
import com.google.android.material.appbar.MaterialToolbar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SearchActivity : AppCompatActivity() {
    private companion object {
        const val SEARCH_EDIT_TEXT = "search_edit_text"
        const val SEARCH_HISTORY_PREFS = "search_history"
        const val TRACKS = "tracks"
        const val API_BASE_URL = "https://itunes.apple.com"
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
    private lateinit var sharedPrefs: SharedPreferences
    private lateinit var sharedPrefsEditor: SharedPrefsEditor
    private lateinit var searchHistory: LinearLayout
    private lateinit var clearHistoryButton: Button
    //endregion

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        initialize()
        setupTextWatcher()
        tracksRecycler.adapter = searchAdapter
        searchHistoryRecycler.adapter = historyAdapter
        historyAdapter.updateTracks(sharedPrefsEditor.getItems(TRACKS))

        backButton.setNavigationOnClickListener {
            finish()
        }

        clearButton.setOnClickListener {
            clearSearchRequest()
        }

        refreshButton.setOnClickListener {
            search()
        }

        searchEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                search()
            }
            false
        }

        searchEditText.setOnFocusChangeListener { view, _ ->
            searchHistory.visibility = searchHistoryVisibility(view, searchEditText.text)
        }

        OnClickSupport.addTo(tracksRecycler).onItemClick(object : OnItemClickListener {
            override fun onItemClick(recyclerView: RecyclerView, position: Int, view: View) {
                historyAdapter.addToHistory(searchAdapter.tracks[position])
                sharedPrefsEditor.addItemList(TRACKS, historyAdapter.tracks)
            }
        })

        clearHistoryButton.setOnClickListener {
            sharedPrefsEditor.clear()
            historyAdapter.updateTracks()
            searchHistory.visibility = View.GONE
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
        searchHistory = findViewById(R.id.search_history)
        clearHistoryButton = findViewById(R.id.clear_history)
        sharedPrefs = getSharedPreferences(SEARCH_HISTORY_PREFS, MODE_PRIVATE)
        sharedPrefsEditor = SharedPrefsEditor(sharedPrefs)
    }

    private fun setupTextWatcher() {
        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {
                clearButton.visibility = clearButtonVisibility(text)
                searchHistory.visibility = searchHistoryVisibility(searchEditText, text)

            }

            override fun afterTextChanged(text: Editable?) {
                searchText = text!!

            }
        }
        searchEditText.addTextChangedListener(textWatcher)
    }

    private fun search() {
        iTunesApi.getTracks(searchText.toString())
            .enqueue(object : Callback<SearchResponse> {
                override fun onResponse(
                    call: Call<SearchResponse>, response: Response<SearchResponse>
                ) {
                    if (response.code() == 200) {
                        if (response.body()?.results?.isNotEmpty() == true) {
                            searchAdapter.updateTracks(response.body()!!.results)
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
        errorView.visibility = View.VISIBLE
    }

    private fun hideErrors() {
        notFoundError.visibility = View.GONE
        serverError.visibility = View.GONE
    }

    private fun clearSearchRequest() {
        searchEditText.setText("")
        val inputMethodManager =
            getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
        inputMethodManager?.hideSoftInputFromWindow(clearButton.windowToken, 0)
        searchEditText.clearFocus()
        searchAdapter.updateTracks()
        hideErrors()
    }

    private fun searchHistoryVisibility(view: View, text: CharSequence?): Int {
        return if (historyAdapter.tracks.isNotEmpty() && view.hasFocus() && text.isNullOrEmpty())
            View.VISIBLE
        else
            View.GONE
    }

    private fun clearButtonVisibility(text: CharSequence?): Int {
        return if (text.isNullOrEmpty())
            View.GONE
        else
            View.VISIBLE
    }
}