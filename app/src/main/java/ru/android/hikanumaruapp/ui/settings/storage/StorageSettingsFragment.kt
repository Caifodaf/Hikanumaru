package ru.android.hikanumaruapp.ui.settings.storage

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import ru.android.hikanumaruapp.R
import ru.android.hikanumaruapp.databinding.FragmentMangaPageBinding
import ru.android.hikanumaruapp.databinding.FragmentStorageSettingsBinding
import ru.android.hikanumaruapp.ui.mangapage.MangaPageViewModel

@AndroidEntryPoint
class StorageSettingsFragment : Fragment() {

    private var _binding: FragmentStorageSettingsBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<StorageSettingsViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentStorageSettingsBinding.inflate(inflater, container, false)
        val root: View = binding.root



        return root
    }

}