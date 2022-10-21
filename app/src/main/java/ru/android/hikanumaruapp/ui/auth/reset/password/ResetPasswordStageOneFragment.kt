package ru.android.hikanumaruapp.ui.auth.reset.password

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import ru.android.hikanumaruapp.R
import ru.android.hikanumaruapp.databinding.FragmentRegistrationBinding
import ru.android.hikanumaruapp.databinding.FragmentResetPasswordStageOneBinding
import ru.android.hikanumaruapp.ui.auth.registration.RegistrationViewModel

@AndroidEntryPoint
class ResetPasswordStageOneFragment : Fragment() {

    private var _binding: FragmentResetPasswordStageOneBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<RegistrationViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentResetPasswordStageOneBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

}