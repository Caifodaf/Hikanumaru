package ru.android.hikanumaruapp.presentasion.settings.about

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ru.android.hikanumaruapp.R

class AboutSettingsFragment : Fragment() {

    companion object {
        fun newInstance() = AboutSettingsFragment()
    }

    private lateinit var viewModel: AboutSettingsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.about_settings_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(AboutSettingsViewModel::class.java)
        // TODO: Use the ViewModel
    }

}