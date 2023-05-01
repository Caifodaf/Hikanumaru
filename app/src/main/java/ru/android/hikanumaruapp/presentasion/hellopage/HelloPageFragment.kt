package ru.android.hikanumaruapp.presentasion.hellopage

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import ru.android.hikanumaruapp.R
import ru.android.hikanumaruapp.api.token.TokenViewModel
import ru.android.hikanumaruapp.databinding.FragmentHelloPageBinding
import ru.android.hikanumaruapp.data.local.user.UserDataViewModel
import ru.android.hikanumaruapp.utilits.NetworkUtils
import kotlin.reflect.jvm.internal.impl.resolve.constants.BooleanValue

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
            true-> getLogin()
            false-> getLogin()
        }
    }

     private var isUser:Boolean = false
     private var isToken:Boolean = false

            private fun getLogin(){
        vmUser.apply { requireActivity().getUserData() }
        vmUser.user.observe(viewLifecycleOwner) { user ->
            vm.isUserLoad = true
            isUser = (user != null)
            Log.d("vmApi", "mangaPageResponse falure - " + user)
            checkedLogin()
        }
        vmToken.token.observe(viewLifecycleOwner) {
            vm.isTokenLoad = true
            isToken = (vmToken.token.value ?: "" != "" && vmToken.token.value ?: "" != null)
            Log.d("vmApi", "mangaPageResponse falure - " + isToken)
            Log.d("vmApi", "mangaPageResponse falure - " + vmToken.token.value)
            checkedLogin()
        }
    }

    private fun checkedLogin() {
        if (vm.isTokenLoad && vm.isUserLoad)
            binding.root.postDelayed({
                when (isToken && isUser) {
                    true -> routeToAppropriatePage(HelloPageViewModel.MAIN)
                    false -> vm.apply {
                        if (requireActivity().onStartup())
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