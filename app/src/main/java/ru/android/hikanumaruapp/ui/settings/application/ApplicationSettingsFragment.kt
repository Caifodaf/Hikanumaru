package ru.android.hikanumaruapp.ui.settings.application

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import ru.android.hikanumaruapp.databinding.FragmentApplicationSettingsBinding
import ru.android.hikanumaruapp.databinding.FragmentMangaPageBinding

@AndroidEntryPoint
class ApplicationSettingsFragment : Fragment() {

    private var _binding: FragmentApplicationSettingsBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<ApplicationSettingsViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentApplicationSettingsBinding.inflate(inflater, container, false)
        val root: View = binding.root



        return root
    }
}