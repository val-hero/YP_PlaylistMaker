package com.example.playlistmaker

import android.content.Context
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
import com.google.android.material.appbar.MaterialToolbar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SearchActivity : AppCompatActivity() {
    companion object {
        const val SEARCH_EDIT_TEXT = "SEARCH_EDIT_TEXT"
        const val API_BASE_URL = "https://itunes.apple.com"
    }

    private val retrofit = Retrofit.Builder()
        .baseUrl(API_BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    //region Variables
    private val iTunesApi = retrofit.create(ITunesApi::class.java)
    private val adapter = TrackAdapter()
    private var searchText: CharSequence = ""
    private lateinit var searchEditText: EditText
    private lateinit var clearButton: ImageView
    private lateinit var backButton: MaterialToolbar
    private lateinit var refreshButton: Button
    private lateinit var notFoundError: LinearLayout
    private lateinit var serverError: LinearLayout
    private lateinit var tracksRecycler: RecyclerView
    //endregion

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        initialize()
        setupTextWatcher()
        tracksRecycler.adapter = adapter

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

    }

    private fun initialize() {
        searchEditText = findViewById(R.id.search_field)
        clearButton = findViewById(R.id.clear_button)
        backButton = findViewById(R.id.search_back_button)
        refreshButton = findViewById(R.id.refresh_button)
        notFoundError = findViewById(R.id.not_found_error)
        serverError = findViewById(R.id.server_error)
        tracksRecycler = findViewById(R.id.track_list)
    }

    private fun setupTextWatcher() {
        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {
                clearButton.visibility = clearButtonVisibility(text)
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
                            adapter.updateTracks(response.body()!!.results)
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
        adapter.updateTracks()
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
        adapter.updateTracks()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putCharSequence(SEARCH_EDIT_TEXT, searchText)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        searchText = savedInstanceState.getCharSequence(SEARCH_EDIT_TEXT, "")
        searchEditText.setText(searchText)
    }

    private fun clearButtonVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) {
            View.GONE
        } else
            View.VISIBLE
    }
}