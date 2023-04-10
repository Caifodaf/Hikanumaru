package ru.android.hikanumaruapp.ui.settings.supprot

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ru.android.hikanumaruapp.R

class SupportSettingsFragment : Fragment() {

    companion object {
        fun newInstance() = SupportSettingsFragment()
    }

    private lateinit var viewModel: SupportSettingsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.support_settings_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(SupportSettingsViewModel::class.java)
        // TODO: Use the ViewModel
    }

}