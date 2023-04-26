package ru.android.hikanumaruapp.presentasion.settings.theme

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ru.android.hikanumaruapp.R

class ThemeSettingsFragment : Fragment() {

    companion object {
        fun newInstance() = ThemeSettingsFragment()
    }

    private lateinit var viewModel: ThemeSettingsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.theme_settings_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ThemeSettingsViewModel::class.java)
        // TODO: Use the ViewModel
    }

}