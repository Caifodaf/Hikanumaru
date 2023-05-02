package ru.android.hikanumaruapp.presentasion.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import ru.android.hikanumaruapp.presentasion.BaseFragment
import ru.android.hikanumaruapp.R
import ru.android.hikanumaruapp.databinding.FragmentSettingsBinding

@AndroidEntryPoint
class SettingsFragment : BaseFragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!
    private val vm by viewModels<SettingsViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupOnBackPressed()
        binding.apply {
            btnInit()
        }
    }

    private fun FragmentSettingsBinding.btnInit(){
        CCAccount.setOnClickListener {
            CCAccount.timerDoubleButton()
            findNavController().navigate(R.id.action_navigation_settings_to_navigation_profile_settings)
        }
        CCNotivication.setOnClickListener {
            CCNotivication.timerDoubleButton()
            findNavController().navigate(R.id.action_navigation_settings_to_navigation_notification_settings)
        }
        CCMedia.setOnClickListener {
            CCMedia.timerDoubleButton()
            findNavController().navigate(R.id.action_navigation_settings_to_navigation_storage_settings)
        }
        CCSource.setOnClickListener {
            CCSource.timerDoubleButton()
            findNavController().navigate(R.id.action_navigation_settings_to_sourceSettingsFragment)
        }
        CCTheme.setOnClickListener {
            CCTheme.timerDoubleButton()
            findNavController().navigate(R.id.action_navigation_settings_to_themeSettingsFragment)
        }
        CCSupport.setOnClickListener {
            CCSupport.timerDoubleButton()
            findNavController().navigate(R.id.action_navigation_settings_to_supportSettingsFragment)
        }
        CCStatistics.setOnClickListener {
            CCStatistics.timerDoubleButton()
            findNavController().navigate(R.id.action_navigation_settings_to_statsSettingsFragment,)
        }
        CCAbout.setOnClickListener {
            CCAbout.timerDoubleButton()
            findNavController().navigate(R.id.action_navigation_settings_to_aboutSettingsFragment)
        }
    }
}