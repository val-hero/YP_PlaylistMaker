package com.example.playlistmaker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageView
import com.example.playlistmaker.R.*

class SearchActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layout.activity_search)

        val inputField = findViewById<EditText>(id.input_field)
        val clearButton = findViewById<ImageView>(id.clear_button)

        clearButton.setOnClickListener {
            inputField.setText("")
        }

        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                clearButton.visibility = clearButtonVisibility(p0)
            }

            override fun afterTextChanged(p0: Editable?) {

            }

        }
        inputField.addTextChangedListener(textWatcher)
    }

    private fun clearButtonVisibility(s: CharSequence?): Int {
        return if(s.isNullOrEmpty()) {
            View.GONE
        } else
            View.VISIBLE
    }
}