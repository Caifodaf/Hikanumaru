package ru.android.hikanumaruapp.ui.radio

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import ru.android.hikanumaruapp.R
import ru.android.hikanumaruapp.databinding.FragmentRadioBinding
import ru.android.hikanumaruapp.ui.home.HomeViewModel

@AndroidEntryPoint
class RadioFragment : Fragment() {

    companion object {
    }

    private var _binding: FragmentRadioBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<RadioViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRadioBinding.inflate(inflater, container, false)
        val root: View = binding.root


        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}