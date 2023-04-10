package ru.android.hikanumaruapp.ui.settings.stats

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ru.android.hikanumaruapp.R

class StatsSettingsFragment : Fragment() {

    companion object {
        fun newInstance() = StatsSettingsFragment()
    }

    private lateinit var viewModel: StatsSettingsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.stats_settings_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(StatsSettingsViewModel::class.java)
        // TODO: Use the ViewModel
    }

}