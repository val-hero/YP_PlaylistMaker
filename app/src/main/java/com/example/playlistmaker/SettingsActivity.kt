package com.example.playlistmaker

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.widget.Toolbar


class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        val back = findViewById<Toolbar>(R.id.back_button)
        back.setNavigationOnClickListener {
            val backIntent = Intent(this, MainActivity::class.java)
            startActivity(backIntent)
        }

        val shareApp = findViewById<TextView>(R.id.share_app)
        shareApp.setOnClickListener {
            val sendIntent = Intent(Intent.ACTION_SEND)
            val message = "https://practicum.yandex.ru/android-developer/"
            sendIntent.putExtra(Intent.EXTRA_TEXT, message)
            sendIntent.type = "text/plain"
            val share = Intent.createChooser(sendIntent, null)
            startActivity(share)
        }

        val supportChat = findViewById<TextView>(R.id.chat_support)
        supportChat.setOnClickListener {
            val sendMailIntent = Intent(Intent.ACTION_SENDTO)
            val subject = "Сообщение разработчикам и разработчицам приложения Playlist Maker"
            val message = "Спасибо разработчикам и разработчицам за крутое приложение!"
            sendMailIntent.data = Uri.parse("mailto:")
            sendMailIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf("valhero@yandex.ru"))
            sendMailIntent.putExtra(Intent.EXTRA_SUBJECT, subject)
            sendMailIntent.putExtra(Intent.EXTRA_TEXT, message)
            val sendMail = Intent.createChooser(sendMailIntent, null)
            startActivity(sendMail)
        }

        val userAgreement = findViewById<TextView>(R.id.user_agreement)
        userAgreement.setOnClickListener {
            val viewIntent = Intent(Intent.ACTION_VIEW)
            val link = "https://yandex.ru/legal/practicum_offer/"
            viewIntent.data = Uri.parse(link)
            val openLink = Intent.createChooser(viewIntent, null)
            startActivity(openLink)
        }
    }
}
