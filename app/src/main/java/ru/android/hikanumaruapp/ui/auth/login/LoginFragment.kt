package ru.android.hikanumaruapp.ui.auth.login

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import ru.android.hikanumaruapp.R
import ru.android.hikanumaruapp.databinding.FragmentLoginBinding
import ru.android.hikanumaruapp.utilits.navigation.Events
import ru.android.hikanumaruapp.utilits.navigation.NavigationFragmentinViewModel
import ru.android.hikanumaruapp.utilits.UIUtils

class LoginFragment : Fragment(),UIUtils {

    companion object{
        const val DEFAULT_BUTTON_VIEW = 0
        const val ACTIVE_BUTTON_VIEW = 1
        const val LOADING_BUTTON_VIEW = 2
        const val ERROR_BUTTON_VIEW = 3
    }

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<LoginViewModel>()

    private var lReqLogin: Boolean = false
    private var lReqPass: Boolean = false
    private var isResetPassword: Boolean = false

    private var isShowPass: Boolean = false

    private val navigationEventsObserver = Events.EventObserver { event ->
        when (event) {
            is NavigationFragmentinViewModel.NavigationFrag -> navigateNav(event.navigation, event.bundle)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        val root: View = binding.root

        initUI()

        return root
    }

    private fun initUI() {

        initBtnNav()
        initBtnClear()

        edLoginListener()
        edPassListener()

    }


    private fun edLoginListener() {
        binding.etLogin.doOnTextChanged { text, start, count, after ->
            binding.ivCheckLogin.visibility = View.VISIBLE
            lReqLogin = if (binding.etLogin.text.toString().length in 3..40) {
                binding.ivCheckLogin.setImageResource(R.drawable.ic_sucess_cirlce)
                true
            } else {
                binding.ivCheckLogin.setImageResource(R.drawable.ic_info_circle_error)
                false
            }
            checkFillingEditTextToLogin()
        }
    }

    private fun edPassListener() {
        binding.etPass.transformationMethod = PasswordTransformationMethod()

        binding.etPass.doOnTextChanged { text, start, count, after ->
            binding.ivEyePass.visibility = View.VISIBLE
            binding.ivCheckPass.visibility = View.VISIBLE
            binding.ivEyePass.setOnClickListener {
                showPassword()
            }
            lReqPass = binding.etPass.text.toString().length in 5..40

            when (lReqPass) {
                true ->  binding.ivCheckPass.setImageResource(R.drawable.ic_sucess_cirlce)
                false ->  binding.ivCheckPass.setImageResource(R.drawable.ic_info_circle_error)
            }
            checkFillingEditTextToLogin()
        }
    }

    private fun showPassword() {
        binding.etPass.transformationMethod = when (isShowPass) {
            true -> {
                isShowPass = false
                binding.ivCheckPass.setImageResource(R.drawable.ic_eye_hide)
                PasswordTransformationMethod()
            }
            false -> {
                isShowPass = true
                binding.ivCheckPass.setImageResource(R.drawable.ic_eye_show)
                HideReturnsTransformationMethod.getInstance()
            }
        }
    }

    @SuppressLint("ResourceAsColor")
    private fun checkFillingEditTextToLogin() {
        if(lReqLogin && lReqPass){
            btnNextStageView(ACTIVE_BUTTON_VIEW)
        } else {
            btnNextStageView(DEFAULT_BUTTON_VIEW)
        }
    }

    private fun initBtnNav() {
        binding.tvResetPass.setOnClickListener {
            timerDoubleBtn(binding.tvResetPass)
            // todo add
            //findNavController().navigate(R.id.action_loginFragment_to_resetPasswordLoginFragment, null)
        }

        binding.tvBtnLogin.setOnClickListener {
            timerDoubleBtn(binding.tvBtnLogin,5000)
            btnNextStageView(LOADING_BUTTON_VIEW)

            observeUser()
            observeError()

            viewModel.postApiAuth(
                login = binding.etLogin.text.toString(),
                pass = binding.etPass.text.toString(),
                requireActivity()
            )

                //viewModel.getCheckEmail(cancel = true)
                //Log.d("ApiService", "apiCheckLogin -$e")
        }

        binding.tvBtnLoginBack.setOnClickListener {
            timerDoubleBtn(binding.tvBtnLoginBack)
            requireActivity().supportFragmentManager.popBackStack()
        }
    }

    private fun initBtnClear(){
        binding.ivBtnClearLogin.setOnClickListener{
            binding.etLogin.text = null
        }
        binding.ivBtnClearPass.setOnClickListener{
            binding.etPass.text = null
        }
    }

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

            btnNextStageView(ERROR_BUTTON_VIEW)
        })
    }

    private fun btnNextStageView(state:Int){
        when (state){
            DEFAULT_BUTTON_VIEW -> {
                binding.tvBtnLogin.setTextColor(resources.getColor(R.color.grey_light_alternative_4));
                DrawableCompat.setTint(binding.tvBtnLogin.background, ContextCompat.getColor(requireContext(), R.color.grey_light_alternative_1));
                binding.tvBtnLogin.isClickable = false
            }
            ACTIVE_BUTTON_VIEW -> {
                binding.tvBtnLogin.setTextColor(resources.getColor(R.color.white));
                DrawableCompat.setTint(binding.tvBtnLogin.background, ContextCompat.getColor(requireContext(), R.color.blue));
                binding.tvBtnLogin.isClickable = true
            }
            LOADING_BUTTON_VIEW -> {
                binding.progressAuthBtn.visibility = View.VISIBLE
                binding.tvBtnLogin.text = ""
                binding.progressAuthBtn.isClickable = false
            }
            ERROR_BUTTON_VIEW -> {
                binding.progressAuthBtn.visibility = View.GONE
                binding.tvBtnLogin.text = getString(R.string.proceed)

                binding.tvBtnLogin.setTextColor(resources.getColor(R.color.grey_light_alternative_4));
                DrawableCompat.setTint(
                    binding.tvBtnLogin.background, ContextCompat.getColor(requireContext(), R.color.grey_light_alternative_1)
                )
                binding.tvBtnLogin.isClickable = false
            }
        }
    }

    fun navigateNav(navigation: Int, bundle: Bundle?) {
        findNavController().navigate(navigation, bundle)
    }


}