package com.example.playlistmaker.sharing.data

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.content.ContextCompat.startActivity
import com.example.playlistmaker.sharing.domain.model.EmailData

class ExternalNavigator(private val context: Context) {

    fun openLink(link: String) {
        val intent = Intent(Intent.ACTION_VIEW).apply { data = Uri.parse(link) }
        startActivity(context, Intent.createChooser(intent, null), null)
    }

    fun openEmail(emailData: EmailData) {
        val intent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:")
            putExtra(Intent.EXTRA_EMAIL, arrayOf(emailData.recipient))
            putExtra(Intent.EXTRA_SUBJECT, emailData.subject)
            putExtra(Intent.EXTRA_TEXT, emailData.message)
        }
        startActivity(context, Intent.createChooser(intent, null), null)
    }

    fun shareLink(link: String) {
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, link)
        }
        startActivity(context, Intent.createChooser(intent, null), null)
    }
}