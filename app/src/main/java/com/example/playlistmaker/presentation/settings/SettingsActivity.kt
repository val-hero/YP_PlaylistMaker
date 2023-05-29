package com.example.playlistmaker.presentation.settings

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.widget.SwitchCompat
import com.example.playlistmaker.utility.App
import com.example.playlistmaker.R
import com.google.android.material.appbar.MaterialToolbar

class SettingsActivity : AppCompatActivity() {
    lateinit var nightTheme: SwitchCompat

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val back = findViewById<MaterialToolbar>(R.id.settings_back_button)
        back.setNavigationOnClickListener {
            finish()
        }

        nightTheme = findViewById(R.id.night_theme_switch)
        matchSwitchToCurrentTheme()
        nightTheme.setOnCheckedChangeListener { _, checked ->
            (applicationContext as App).switchTheme(checked)
        }

        val shareApp = findViewById<TextView>(R.id.share_app)
        shareApp.setOnClickListener {
            val sendIntent = Intent(Intent.ACTION_SEND)
            val message = getString(R.string.share_app_link)
            sendIntent.putExtra(Intent.EXTRA_TEXT, message)
            sendIntent.type = "text/plain"
            val share = Intent.createChooser(sendIntent, null)
            startActivity(share)
        }

        val supportChat = findViewById<TextView>(R.id.chat_support)
        supportChat.setOnClickListener {
            val sendMailIntent = Intent(Intent.ACTION_SENDTO)
            val subject = getString(R.string.mail_to_devs_subject)
            val message = getString(R.string.mail_to_devs_message)
            val receiver = getString(R.string.mail_to_devs_receiver)
            sendMailIntent.data = Uri.parse("mailto:")
            sendMailIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(receiver))
            sendMailIntent.putExtra(Intent.EXTRA_SUBJECT, subject)
            sendMailIntent.putExtra(Intent.EXTRA_TEXT, message)
            val sendMail = Intent.createChooser(sendMailIntent, null)
            startActivity(sendMail)
        }

        val userAgreement = findViewById<TextView>(R.id.user_agreement)
        userAgreement.setOnClickListener {
            val viewIntent = Intent(Intent.ACTION_VIEW)
            val link = getString(R.string.user_agreement_link)
            viewIntent.data = Uri.parse(link)
            val openLink = Intent.createChooser(viewIntent, null)
            startActivity(openLink)
        }
    }

    private fun matchSwitchToCurrentTheme() {
        val appContext = applicationContext as App
        nightTheme.isChecked =
            appContext.sharedPrefs.getBoolean(App.DARK_THEME_ENABLED, appContext.systemThemeIsDark)
    }
}
