package com.example.playlistmaker.settings.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.playlistmaker.R
import com.example.playlistmaker.core.App
import com.example.playlistmaker.databinding.FragmentSettingsBinding
import com.example.playlistmaker.settings.ui.viewmodel.SettingsViewModel
import com.example.playlistmaker.sharing.domain.model.EmailData
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsFragment : Fragment() {
    private lateinit var binding: FragmentSettingsBinding
    private val viewModel by viewModel<SettingsViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.appThemeIsDark.observe(viewLifecycleOwner) {
            binding.nightThemeSwitch.isChecked = it
            (requireActivity().applicationContext as App).switchTheme(it)
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
