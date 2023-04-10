package ru.android.hikanumaruapp.ui.settings

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import ru.android.hikanumaruapp.BaseFragment
import ru.android.hikanumaruapp.R
import ru.android.hikanumaruapp.databinding.FragmentLoginBinding
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
        requireActivity().findViewById<ConstraintLayout>(R.id.CCSearchTab).visibility = View.GONE

        binding.apply {
            btnInit()
        }
    }

    private fun FragmentSettingsBinding.btnInit(){
        CCAccount.setOnClickListener {
            CCAccount.timerDoubleBtn()
            findNavController().navigate(R.id.action_navigation_settings_to_navigation_profile_settings, null)
        }
        CCNotivication.setOnClickListener {
            CCNotivication.timerDoubleBtn()
            findNavController().navigate(R.id.action_navigation_settings_to_navigation_notification_settings, null)
        }
        CCMedia.setOnClickListener {
            CCMedia.timerDoubleBtn()
            findNavController().navigate(R.id.action_navigation_settings_to_navigation_storage_settings, null)
        }
        CCSource.setOnClickListener {
            CCSource.timerDoubleBtn()
            findNavController().navigate(R.id.action_navigation_settings_to_sourceSettingsFragment, null)
        }
        CCTheme.setOnClickListener {
            CCTheme.timerDoubleBtn()
            findNavController().navigate(R.id.action_navigation_settings_to_themeSettingsFragment, null)
        }
        CCSupport.setOnClickListener {
            CCSupport.timerDoubleBtn()
            findNavController().navigate(R.id.action_navigation_settings_to_supportSettingsFragment, null)
        }
        CCStatistics.setOnClickListener {
            CCStatistics.timerDoubleBtn()
            findNavController().navigate(R.id.action_navigation_settings_to_statsSettingsFragment, null)
        }
        CCAbout.setOnClickListener {
            CCAbout.timerDoubleBtn()
            findNavController().navigate(R.id.action_navigation_settings_to_aboutSettingsFragment, null)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun View.timerDoubleBtn(time: Long = 2000) {
        isClickable = false
        Handler(Looper.getMainLooper())
        handler.postDelayed({
            isClickable = true
        }, time)
    }
}