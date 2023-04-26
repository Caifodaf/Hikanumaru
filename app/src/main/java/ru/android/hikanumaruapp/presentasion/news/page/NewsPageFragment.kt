package ru.android.hikanumaruapp.presentasion.news.page

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import ru.android.hikanumaruapp.databinding.FragmentNewsPageBinding

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