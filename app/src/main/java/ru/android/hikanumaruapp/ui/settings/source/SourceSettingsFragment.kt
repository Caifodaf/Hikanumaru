package ru.android.hikanumaruapp.ui.settings.source

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ru.android.hikanumaruapp.R

class SourceSettingsFragment : Fragment() {

    companion object {
        fun newInstance() = SourceSettingsFragment()
    }

    private lateinit var viewModel: SourceSettingsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.source_settings_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(SourceSettingsViewModel::class.java)
        // TODO: Use the ViewModel
    }

}