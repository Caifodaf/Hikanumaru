package ru.android.hikanumaruapp.ui.news.page

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
import ru.android.hikanumaruapp.databinding.FragmentNewsPageBinding
import ru.android.hikanumaruapp.ui.mangapage.MangaPageViewModel

@AndroidEntryPoint
class NewsPageFragment : Fragment() {

    private var _binding: FragmentNewsPageBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<NewsPageViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNewsPageBinding.inflate(inflater, container, false)
        val root: View = binding.root





        return root
    }

}