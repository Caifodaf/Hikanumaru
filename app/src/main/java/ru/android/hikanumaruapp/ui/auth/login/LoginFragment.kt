package ru.android.hikanumaruapp.ui.auth.login

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import ru.android.hikanumaruapp.R
import ru.android.hikanumaruapp.databinding.FragmentLoginBinding
import ru.android.hikanumaruapp.ui.auth.registration.state.one.RegistrationViewModel
import ru.android.hikanumaruapp.utilits.navigation.Events
import ru.android.hikanumaruapp.utilits.navigation.NavigationFragmentinViewModel
import ru.android.hikanumaruapp.utilits.UIUtils

class LoginFragment : Fragment(),UIUtils {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<LoginViewModel>()

    lateinit var handler: Handler
    private var isShowPass: Boolean = false

    private val navigationEventsObserver = Events.EventObserver { event ->
        when (event) {
            is NavigationFragmentinViewModel.NavigationFrag -> navigateNav(event.navigation, event.bundle)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            checkFillingEditTextToLogin()

            initBtnNav()
            initBtnClear()

            edLoginListener()
            edPassListener()
        }
    }

    private fun FragmentLoginBinding.edLoginListener() {
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

    private fun FragmentLoginBinding.edPassListener() {
        ETPass.transformationMethod = PasswordTransformationMethod()

        ETPass.doOnTextChanged { text, start, count, after ->
            ImageCheckPass.apply {
                visibility = View.VISIBLE
                setImageResource(when (isValidPassword()) {
                    true -> R.drawable.ic_sucess_cirlce
                    else -> R.drawable.ic_info_circle_error
                })
            }
            ImageEyePass.setOnClickListener { showPassword() }
            checkFillingEditTextToLogin()
        }
    }

    private fun FragmentLoginBinding.showPassword() {
        isShowPass = !isShowPass
        val transformationMethod = when (isShowPass) {
            true -> HideReturnsTransformationMethod.getInstance()
            else -> PasswordTransformationMethod.getInstance()
        }

        ETPass.transformationMethod = transformationMethod
        ETPass.transformationMethod = transformationMethod
        ImageEyePass.setImageResource(if (isShowPass) R.drawable.ic_eye_show else R.drawable.ic_eye_hide)
    }

    private fun FragmentLoginBinding.initBtnClear(){
        ImageBtnClearLogin.setOnClickListener{
            ETLogin.text = null
        }
        ImageBtnClearPass.setOnClickListener{
            ETPass.text = null
        }
        CCMain.setOnClickListener{
            val imm: InputMethodManager =
                requireActivity().getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            var view = requireActivity().currentFocus
            if (view == null) view = View(activity)
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    /////////////////////////////////////////////////////////////////
    private fun observeUser(){
        // todo useless
    }


    private fun observeError(){
        viewModel.error.observe(viewLifecycleOwner, Observer { error ->
            var message = error.message
            if (message.isNullOrEmpty()) {
                message = "Unknown Error" as String
            }

            when (error.codeApp) {
                401 -> {
                    message = "Неверный логин или пароль."

                    // todo disable pop
                    //errorPop(message!!, errorLayout, inflater)
                }
                0->{
                    // todo disable pop
                    //errorPop("Ошибка авторизации. Код: $codeApi", errorLayout, inflater)
                }
                else -> {
                    // todo disable pop
                    //errorPop("Ошибка отправки. Код: $codeApi", errorLayout, inflater)
                }
            }

            //vm.btnNextStageView(RegistrationViewModel.ERROR_BUTTON_VIEW)
        })
    }
    /////////////////////////////////////////////////////////////////

    private fun FragmentLoginBinding.isValidLogin() =
        ETLogin.text.length in RegistrationViewModel.LENGTH_LOGIN_RANGE

    private fun FragmentLoginBinding.isValidPassword() =
        ETPass.text.length in RegistrationViewModel.LENGTH_PASSWORD_RANGE

    @SuppressLint("ResourceAsColor")
    fun FragmentLoginBinding.checkFillingEditTextToLogin(){
        when(isValidLogin() && isValidPassword()){
            true -> btnNextStageView(RegistrationViewModel.ACTIVE_BUTTON_VIEW)
            else -> btnNextStageView(RegistrationViewModel.DEFAULT_BUTTON_VIEW)
        }
    }

    private fun FragmentLoginBinding.initBtnNav() {
        TVBtnResetPassword.setOnClickListener {
            TVBtnResetPassword.timerDoubleBtn()
            findNavController().navigate(R.id.action_navigation_login_to_navigation_reset_password_stage_three, null)
        }

        TVBtnLogin.setOnClickListener {
            TVBtnLogin.timerDoubleBtn()
            btnNextStageView(RegistrationViewModel.LOADING_BUTTON_VIEW)

            observeUser()
            observeError()

            viewModel.postApiAuth(
                login = ETLogin.text.toString(),
                pass =  ETPass.text.toString(),
                requireActivity()
            )
        }

        TVBtnLoginBack.setOnClickListener {
            TVBtnLoginBack.timerDoubleBtn()
            findNavController().navigateUp()
        }
    }

    private fun FragmentLoginBinding.btnNextStageView(state:Int){
        val context = requireContext()
        when (state){
            RegistrationViewModel.DEFAULT_BUTTON_VIEW -> {
                with(TVBtnLogin) {
                    setTextColor(ContextCompat.getColor(context, R.color.grey_light_alternative_4))
                    isClickable = false
                }
                DrawableCompat.setTint(TVBtnLogin.background, ContextCompat.getColor(context, R.color.grey_light_alternative_1))
                ProgressAuth.visibility = View.GONE
            }
            RegistrationViewModel.ACTIVE_BUTTON_VIEW -> {
                with(TVBtnLogin) {
                    setTextColor(ContextCompat.getColor(context, R.color.white))
                    isClickable = true
                }
                DrawableCompat.setTint(TVBtnLogin.background, ContextCompat.getColor(context, R.color.grey_light_alternative_7))
                ProgressAuth.visibility = View.GONE
            }
            RegistrationViewModel.LOADING_BUTTON_VIEW -> {
                TVBtnLogin.text = ""
                ProgressAuth.isClickable = false
                ProgressAuth.visibility = View.VISIBLE
            }
            RegistrationViewModel.ERROR_BUTTON_VIEW -> {
                TVBtnLogin.text = getString(R.string.log_in)
                with(TVBtnLogin) {
                    setTextColor(ContextCompat.getColor(context, R.color.grey_light_alternative_4))
                    isClickable = false
                }
                DrawableCompat.setTint(TVBtnLogin.background, ContextCompat.getColor(context, R.color.grey_light_alternative_1))
                ProgressAuth.visibility = View.GONE
            }
        }
    }

    fun navigateNav(navigation: Int, bundle: Bundle?) {
        findNavController().navigate(navigation, bundle)
    }

    fun View.timerDoubleBtn(time: Long = 2000) {
        isClickable = false
        this@LoginFragment.handler = Handler(Looper.getMainLooper())
        handler.postDelayed({
            isClickable = true
        }, time)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        if (::handler.isInitialized)
            handler.removeCallbacksAndMessages(null)
    }
}