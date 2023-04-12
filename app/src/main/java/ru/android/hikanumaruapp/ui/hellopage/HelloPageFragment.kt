package ru.android.hikanumaruapp.ui.hellopage

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import ru.android.hikanumaruapp.R
import ru.android.hikanumaruapp.api.api.main.MainApiViewModel
import ru.android.hikanumaruapp.api.token.TokenViewModel
import ru.android.hikanumaruapp.databinding.FragmentHelloPageBinding
import ru.android.hikanumaruapp.local.user.UserDataViewModel
import ru.android.hikanumaruapp.utilits.NetworkUtils

@AndroidEntryPoint
class HelloPageFragment : Fragment() {

    private var _binding: FragmentHelloPageBinding? = null
    private val binding get() = _binding!!
    private val vm by viewModels<HelloPageViewModel>()

    private val vmToken by activityViewModels<TokenViewModel>()
    private val vmUser by viewModels<UserDataViewModel>()

    private var isConnection = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentHelloPageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        isConnection = NetworkUtils.isInternetAvailable(requireContext())

        when(isConnection){
            true-> checkedLogin()
            false-> checkedLogin()
        }
    }

    private fun checkedLogin(){
        val isToken = (vmToken.token ?: "" != "" && vmToken.token ?: "" != null)
        val user = vmUser.apply { requireActivity().getUserData() }
        val isUser = (user != null)

        binding.root.postDelayed({
            when (isToken && isUser) {
                true -> routeToAppropriatePage(HelloPageViewModel.MAIN)
                false -> {
                    var isStart = false
                    vm.apply {
                        isStart = requireActivity().onStartup()
                    }
                    if (isStart)
                        routeToAppropriatePage(HelloPageViewModel.START)
                    else
                        routeToAppropriatePage(HelloPageViewModel.LOGIN)
                }
            }
        }, 1000L)
    }


    private fun routeToAppropriatePage(user: Int) {
        when(user) {
            HelloPageViewModel.START -> findNavController().navigate(R.id.action_navigation_hello_page_to_navigation_start_page)
            HelloPageViewModel.LOGIN -> findNavController().navigate(R.id.action_navigation_hello_page_to_navigation_login)
            HelloPageViewModel.MAIN -> findNavController().navigate(R.id.action_navigation_hello_page_to_navigation_home)
            else -> findNavController().navigate(R.id.action_navigation_hello_page_to_navigation_start_page)
        }
    }

}