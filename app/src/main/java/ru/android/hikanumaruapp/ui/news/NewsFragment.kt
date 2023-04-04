package ru.android.hikanumaruapp.ui.news

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import ru.android.hikanumaruapp.BaseFragment
import ru.android.hikanumaruapp.R
import ru.android.hikanumaruapp.databinding.FragmentNewsBinding

@AndroidEntryPoint
class NewsFragment : BaseFragment() {

    companion object {
    }

    private var _binding: FragmentNewsBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<NewsViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNewsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        requireActivity().findViewById<ConstraintLayout>(R.id.CCSearchTab).visibility = View.GONE
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupOnBackPressed()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}