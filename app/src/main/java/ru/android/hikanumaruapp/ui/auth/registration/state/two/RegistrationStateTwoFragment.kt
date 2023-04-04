package ru.android.hikanumaruapp.ui.auth.registration.state.two

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnFocusChangeListener
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import ru.android.hikanumaruapp.R
import ru.android.hikanumaruapp.databinding.FragmentRegistrationStateTwoBinding
import ru.android.hikanumaruapp.ui.auth.registration.state.one.RegistrationViewModel
import ru.android.hikanumaruapp.utilits.navigation.Events
import ru.android.hikanumaruapp.utilits.navigation.NavigationFragmentinViewModel


class RegistrationStateTwoFragment() : Fragment() {

    private var _binding: FragmentRegistrationStateTwoBinding? = null
    private val binding get() = _binding!!
    private val vm by viewModels<RegistrationStateTwoViewModel>()

    lateinit var handler: Handler

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
            TVNextStageReg.timerDoubleBtn()
            btnNextStageView(RegistrationViewModel.LOADING_BUTTON_VIEW)

            //TODO NOt Founded Now
            //vm.apiCheckLogin(
            //    login = ETLogin.text.toString(),
            //    userName = ETNameReg.text.toString()
            //)
           vm.setDataStageTwo(
               login = ETLogin.text.toString(),
               userName = ETNameReg.text.toString()
           )
           vm.postCreateUser(requireActivity())
        }

        TVBtnBackReg.setOnClickListener {
            TVBtnBackReg.timerDoubleBtn()
            findNavController().navigateUp()
        }
    }

    private fun FragmentRegistrationStateTwoBinding.initBtnClear(){
        ImageBtnClearLogin.setOnClickListener{
            ETLogin.setText("@")
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
        vm.loginResponse.observe(viewLifecycleOwner) { response ->
            when(response.success){
                true->{
                    changeCheckImage(true)
                    vm.postCreateUser(requireActivity())
                    //btnNextStageView(RegistrationFragment.ACTIVE_BUTTON_VIEW)
                    //findNavController().navigate(R.id.navigation_registration_two, null)
                }
                false->{
                    // todo disable pop
                    //errorPop(response.body()!!.message,errorLayout,inflater)
                    changeCheckImage(false)
                    btnNextStageView(RegistrationViewModel.DEFAULT_BUTTON_VIEW)
                }
            }
        }
    }

    private fun FragmentRegistrationStateTwoBinding.observeError(){
        vm.loginError.observe(viewLifecycleOwner, Observer { error ->
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
        ETLogin.text.length in RegistrationViewModel.LENGTH_LOGIN_RANGE

    private fun FragmentRegistrationStateTwoBinding.isValidName() =
        ETLogin.text.length in RegistrationViewModel.LENGTH_NAME_RANGE

    @SuppressLint("ResourceAsColor")
    fun FragmentRegistrationStateTwoBinding.checkFillingEditTextToLogin(){
        when(isValidLogin() && isValidName()){
            true -> btnNextStageView(RegistrationViewModel.ACTIVE_BUTTON_VIEW)
            else -> btnNextStageView(RegistrationViewModel.DEFAULT_BUTTON_VIEW)
        }
    }

    private fun FragmentRegistrationStateTwoBinding.btnNextStageView(state:Int){
        val context = requireContext()
        when (state){
            RegistrationViewModel.DEFAULT_BUTTON_VIEW -> {
                with(TVNextStageReg) {
                    text = getString(R.string.proceed)
                    setTextColor(ContextCompat.getColor(context, R.color.grey_light_alternative_4))
                    isClickable = false
                }
                DrawableCompat.setTint(TVNextStageReg.background, ContextCompat.getColor(context, R.color.grey_light_alternative_1))
                ProgressAuth.visibility = View.GONE
            }
            RegistrationViewModel.ACTIVE_BUTTON_VIEW -> {
                with(TVNextStageReg) {
                    text = getString(R.string.proceed)
                    setTextColor(ContextCompat.getColor(context, R.color.white))
                    isClickable = true
                }
                DrawableCompat.setTint(TVNextStageReg.background, ContextCompat.getColor(context, R.color.grey_light_alternative_7))
                ProgressAuth.visibility = View.GONE
            }
            RegistrationViewModel.LOADING_BUTTON_VIEW -> {
                TVNextStageReg.text = ""
                ProgressAuth.isClickable = false
                ProgressAuth.visibility = View.VISIBLE
            }
            RegistrationViewModel.ERROR_BUTTON_VIEW -> {
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

    fun View.timerDoubleBtn(time: Long = 2000) {
        isClickable = false
        this@RegistrationStateTwoFragment.handler = Handler(Looper.getMainLooper())
        handler.postDelayed({
            isClickable = true
        }, time)
    }

    fun navigateNav(navigation: Int, bundle: Bundle?) {
        findNavController().navigate(navigation, bundle)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        if (::handler.isInitialized)
            handler.removeCallbacksAndMessages(null)
    }
}