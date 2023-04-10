package ru.android.hikanumaruapp.ui.hellopage

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import ru.android.hikanumaruapp.R
import ru.android.hikanumaruapp.databinding.FragmentHelloPageBinding

@AndroidEntryPoint
class HelloPageFragment : Fragment() {

    private var _binding: FragmentHelloPageBinding? = null
    private val binding get() = _binding!!
    private val vm by viewModels<HelloPageViewModel>()

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
        val sp = requireActivity().getPreferences(Context.MODE_PRIVATE)
        scheduleSplashScreen(vm.onStartup(sp),sp)
    }

    private fun scheduleSplashScreen(ifFirstStart: Boolean, sp: SharedPreferences) {
        binding.root.postDelayed({
            val user = vm.getUser(ifFirstStart,sp)
            routeToAppropriatePage(user)
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