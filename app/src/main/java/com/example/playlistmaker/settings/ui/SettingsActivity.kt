package com.example.playlistmaker.settings.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivitySettingsBinding
import com.example.playlistmaker.settings.ui.viewmodel.SettingsViewModel
import com.example.playlistmaker.sharing.domain.model.EmailData
import com.example.playlistmaker.utility.App
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySettingsBinding
    private val viewModel by viewModel<SettingsViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.appThemeIsDark.observe(this) {
            binding.nightThemeSwitch.isChecked = it
            (applicationContext as App).switchTheme(it)
        }

        binding.settingsBackButton.setNavigationOnClickListener {
            finish()
        }

        binding.nightThemeSwitch.setOnCheckedChangeListener { _, checked ->
            viewModel.switchTheme(checked)
        }

        binding.shareApp.setOnClickListener {
            viewModel.shareApp(getString(R.string.share_app_link))
        }

        binding.chatSupport.setOnClickListener {
            viewModel.sendMailToSupport(
                EmailData(
                    recipient = getString(R.string.mail_to_devs_recipient),
                    subject = getString(R.string.mail_to_devs_subject),
                    message = getString(R.string.mail_to_devs_message)
                )
            )
        }

        binding.userAgreement.setOnClickListener {
            viewModel.openTerms(getString(R.string.user_agreement_link))
        }
    }
}
