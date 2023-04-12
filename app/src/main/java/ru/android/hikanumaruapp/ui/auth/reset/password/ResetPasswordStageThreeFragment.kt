package ru.android.hikanumaruapp.ui.auth.reset.password

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import ru.android.hikanumaruapp.databinding.FragmentResetPasswordStageThreeBinding
import ru.android.hikanumaruapp.ui.auth.registration.RegistrationViewModel

@AndroidEntryPoint
class ResetPasswordStageThreeFragment : Fragment() {

    private var _binding: FragmentResetPasswordStageThreeBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<RegistrationViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentResetPasswordStageThreeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

}