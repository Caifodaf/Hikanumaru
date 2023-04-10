package ru.android.hikanumaruapp.ui.auth.registration.state.two

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnFocusChangeListener
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import ru.android.hikanumaruapp.BaseFragment
import ru.android.hikanumaruapp.R
import ru.android.hikanumaruapp.api.api.main.MainApiViewModel
import ru.android.hikanumaruapp.api.api.token.AuthViewModel
import ru.android.hikanumaruapp.api.init.ApiResponse
import ru.android.hikanumaruapp.api.token.TokenViewModel
import ru.android.hikanumaruapp.databinding.FragmentRegistrationStateTwoBinding
import ru.android.hikanumaruapp.ui.auth.RulesNameAuth
import ru.android.hikanumaruapp.utilits.navigation.Events
import ru.android.hikanumaruapp.utilits.navigation.NavigationFragmentinViewModel

@AndroidEntryPoint
class RegistrationStateTwoFragment() : BaseFragment() {

    private var _binding: FragmentRegistrationStateTwoBinding? = null
    private val binding get() = _binding!!
    private val vm by viewModels<RegistrationStateTwoViewModel>()

    private val vmAuth by viewModels<AuthViewModel>()
    private val vmToken by activityViewModels<TokenViewModel>()
    private val vmApi by viewModels<MainApiViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentRegistrationStateTwoBinding.inflate(inflater, container, false)
        return binding.root
    }

    private val navigationEventsObserver = Events.EventObserver { event ->
        when (event) {
            is NavigationFragmentinViewModel.NavigationFrag -> navigateNav(event.navigation, event.bundle)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        argumentsLoader()

        // TODO DEBUG
        binding.TVNextStageReg.isClickable = true

        binding.apply {
            checkFillingEditTextToLogin()

            initBtnNav()
            initBtnClear()

            edLoginListener()
            edNameListener()

            observeError()
            observeLogin()
        }
    }

    private fun argumentsLoader(){
        vm.setDataStageOne(
            email = requireArguments().getString("email").toString(),
            pass = requireArguments().getString("pass").toString()
        )
    }

    private fun FragmentRegistrationStateTwoBinding.edLoginListener() {
        ETLogin.onFocusChangeListener = OnFocusChangeListener { v, hasFocus ->
            if (hasFocus && ETLogin.text.isNullOrBlank())
                ETLogin.setText("@")
        }
        ETLogin.doOnTextChanged { text, start, count, after ->
            val text: String = ETLogin.text.toString().lowercase()
            if (ETLogin.text.toString() != text) {
                ETLogin.setText(text)
                ETLogin.setSelection(text.length)
            }
            ImageCheckLogin.apply {
                visibility = View.VISIBLE
                setImageResource(when (isValidLogin()) {
                    true -> R.drawable.ic_sucess_cirlce
                    else -> R.drawable.ic_info_circle_error
                })
            }
            checkFillingEditTextToLogin()
        }
    }

    private fun FragmentRegistrationStateTwoBinding.edNameListener() {
        ETNameReg.doOnTextChanged { text, start, count, after ->
            ImageCheckName.apply {
                visibility = View.VISIBLE
                setImageResource(when (isValidName()) {
                    true -> R.drawable.ic_sucess_cirlce
                    else -> R.drawable.ic_info_circle_error
                })
            }
            checkFillingEditTextToLogin()
        }
    }

    private fun FragmentRegistrationStateTwoBinding.initBtnNav() {
       TVNextStageReg.setOnClickListener {
            TVNextStageReg.timerDoubleButton()
            btnNextStageView(RulesNameAuth.LOADING_BUTTON_VIEW)

            //TODO NOt Founded Now
            //vm.apiCheckLogin(
            //    login = ETLogin.text.toString(),
            //    userName = ETNameReg.text.toString()
            //)
           vm.setDataStageTwo(
               login = ETLogin.text.toString(),
               userName = ETNameReg.text.toString()
           )
           vm.postApiCreateUser(vmAuth)
        }

        TVBtnBackReg.setOnClickListener {
            TVBtnBackReg.timerDoubleButton()
            findNavController().navigateUp()
        }
    }

    private fun FragmentRegistrationStateTwoBinding.initBtnClear(){
        ImageBtnClearLogin.setOnClickListener{
            ETLogin.text = null
        }
        ImageBtnClearName.setOnClickListener{
            ETNameReg.text = null
        }
        CCMain.setOnClickListener{
            val imm: InputMethodManager =
                requireActivity().getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            var view = requireActivity().currentFocus
            if (view == null) view = View(activity)
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    private fun FragmentRegistrationStateTwoBinding.changeCheckImage(check:Boolean,optional:Int=0) {
        ImageCheckLogin.visibility = View.VISIBLE
        when (check) {
            true->  ImageCheckLogin.setImageResource(R.drawable.ic_sucess_cirlce)
            false-> ImageCheckLogin.setImageResource(R.drawable.ic_info_circle_error)
        }
        if (optional == 1)
            ImageCheckLogin.setImageResource(R.drawable.ic_load_alt)
    }

    /////////////////////////////////////////////////////////////////
    private fun FragmentRegistrationStateTwoBinding.observeLogin(){
        vmAuth.createUserResponse.observe(viewLifecycleOwner) {
            when(it) {
                is ApiResponse.Failure -> {
                    Log.d("pizdaaa","falure - " + it.errorMessage)
                    // todo disable pop
                    //errorPop(response.body()!!.message,errorLayout,inflater)
                    changeCheckImage(false)
                    btnNextStageView(RulesNameAuth.DEFAULT_BUTTON_VIEW)
                }
                ApiResponse.Loading -> {
                    Log.d("pizdaaa","Loading")
                }
                is ApiResponse.Success -> {
                    vmToken.saveToken(it.data.token)
                    vm.apiAuthGetUser(vmApi)
                    Log.d("pizdaaa","token - " + it.data.token)
                    Log.d("pizdaaa","refresh - " + it.data.refresh)

                    changeCheckImage(true)
                    //vm.postCreateUser(requireActivity())
                    //btnNextStageView(RegistrationFragment.ACTIVE_BUTTON_VIEW)
                    //findNavController().navigate(R.id.navigation_registration_two, null)
                }
            }
        }
    }

    private fun FragmentRegistrationStateTwoBinding.observeError(){
        vm.error.observe(viewLifecycleOwner, Observer { error ->
            //val transaction: FragmentTransaction = parentFragmentManager.beginTransaction()
            //val bundle = Bundle()
//
            //bundle.putString("error","Ошибка при регистрации.")
//
            //findNavController().navigate(R.id.action_navigation_registration_two_to_navigation_start_page, bundle)
            //transaction.remove(this@RegistrationStateTwoFragment)
            //transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE)
            //transaction.commit()
        })
    }
    /////////////////////////////////////////////////////////////////

    private fun FragmentRegistrationStateTwoBinding.isValidLogin() =
        ETLogin.text.length in RulesNameAuth.LENGTH_LOGIN_RANGE

    private fun FragmentRegistrationStateTwoBinding.isValidName() =
        ETLogin.text.length in RulesNameAuth.LENGTH_NAME_RANGE

    @SuppressLint("ResourceAsColor")
    fun FragmentRegistrationStateTwoBinding.checkFillingEditTextToLogin(){
        when(isValidLogin() && isValidName()){
            true -> btnNextStageView(RulesNameAuth.ACTIVE_BUTTON_VIEW)
            else -> btnNextStageView(RulesNameAuth.DEFAULT_BUTTON_VIEW)
        }
    }

    private fun FragmentRegistrationStateTwoBinding.btnNextStageView(state:Int){
        val context = requireContext()
        when (state){
            RulesNameAuth.DEFAULT_BUTTON_VIEW -> {
                with(TVNextStageReg) {
                    text = getString(R.string.proceed)
                    setTextColor(ContextCompat.getColor(context, R.color.grey_light_alternative_4))
                    isClickable = false
                }
                DrawableCompat.setTint(TVNextStageReg.background, ContextCompat.getColor(context, R.color.grey_light_alternative_1))
                ProgressAuth.visibility = View.GONE
            }
            RulesNameAuth.ACTIVE_BUTTON_VIEW -> {
                with(TVNextStageReg) {
                    text = getString(R.string.proceed)
                    setTextColor(ContextCompat.getColor(context, R.color.white))
                    isClickable = true
                }
                DrawableCompat.setTint(TVNextStageReg.background, ContextCompat.getColor(context, R.color.grey_light_alternative_7))
                ProgressAuth.visibility = View.GONE
            }
            RulesNameAuth.LOADING_BUTTON_VIEW -> {
                TVNextStageReg.text = ""
                ProgressAuth.isClickable = false
                ProgressAuth.visibility = View.VISIBLE
            }
            RulesNameAuth.ERROR_BUTTON_VIEW -> {
                TVNextStageReg.text = getString(R.string.proceed)
                with(TVNextStageReg) {
                    setTextColor(ContextCompat.getColor(context, R.color.grey_light_alternative_4))
                    isClickable = false
                }
                DrawableCompat.setTint(TVNextStageReg.background, ContextCompat.getColor(context, R.color.grey_light_alternative_1))
                ProgressAuth.visibility = View.GONE
            }
        }
    }

    fun navigateNav(navigation: Int, bundle: Bundle?) {
        findNavController().navigate(navigation, bundle)
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }
}