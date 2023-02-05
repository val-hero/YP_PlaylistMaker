package com.example.playlistmaker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.View.OnClickListener
import android.widget.Button
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val searchButton = findViewById<Button>(R.id.song_search_button)
        val searchClick: View.OnClickListener = object : View.OnClickListener {
            override fun onClick(p0: View?) {
                Toast.makeText(this@MainActivity, "Search button is pressed", Toast.LENGTH_SHORT)
                    .show()
            }
        }

        searchButton.setOnClickListener(searchClick)

        val libraryButton = findViewById<Button>(R.id.library_button)
        libraryButton.setOnClickListener {
            Toast.makeText(this@MainActivity, "Library button is pressed", Toast.LENGTH_SHORT)
                .show()
        }

        val settingsButton = findViewById<Button>(R.id.settings_button)
        settingsButton.setOnClickListener {
            Toast.makeText(this@MainActivity, "Settings button is pressed", Toast.LENGTH_SHORT)
                .show()
        }
    }
}