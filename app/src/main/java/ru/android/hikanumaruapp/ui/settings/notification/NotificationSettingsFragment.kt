package ru.android.hikanumaruapp.ui.settings.notification

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
import ru.android.hikanumaruapp.databinding.FragmentNotificationSettingsBinding
import ru.android.hikanumaruapp.ui.mangapage.MangaPageViewModel

@AndroidEntryPoint
class NotificationSettingsFragment : Fragment() {

    private var _binding: FragmentNotificationSettingsBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<NotificationSettingsViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNotificationSettingsBinding.inflate(inflater, container, false)
        val root: View = binding.root



        return root
    }


}