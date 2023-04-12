package ru.android.hikanumaruapp.ui.auth.login

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import ru.android.hikanumaruapp.BaseFragment
import ru.android.hikanumaruapp.R
import ru.android.hikanumaruapp.api.api.main.MainApiViewModel
import ru.android.hikanumaruapp.api.api.token.AuthViewModel
import ru.android.hikanumaruapp.api.init.ApiResponse
import ru.android.hikanumaruapp.api.token.TokenViewModel
import ru.android.hikanumaruapp.databinding.FragmentLoginBinding
import ru.android.hikanumaruapp.local.user.UserDataViewModel
import ru.android.hikanumaruapp.ui.auth.RulesNameAuth
import ru.android.hikanumaruapp.ui.auth.registration.state.two.RegistrationStateTwoFragment
import ru.android.hikanumaruapp.utilits.popdialog.PopAlertDialog


@AndroidEntryPoint
class LoginFragment : BaseFragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private val vm by viewModels<LoginViewModel>()

    private val vmAuth by viewModels<AuthViewModel>()
    private val vmToken by activityViewModels<TokenViewModel>()
    private val vmApi by viewModels<MainApiViewModel>()
    private val vmUser by viewModels<UserDataViewModel>()

    private var isShowPass: Boolean = false

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
        setupOnBackPressed()
        getCheckErrorArgument()
        binding.apply {
            checkFillingEditTextToLogin()

            initBtnNav()
            initBtnClear()
            initButtonsServices()

            edLoginListener()
            edPassListener()

            observeLogin()
            observeUser()
        }
    }

    private fun getCheckErrorArgument() {
        val registrationError =
            arguments?.getBoolean(RegistrationStateTwoFragment.BUNDLE_ERROR_CODE, false)
        if (registrationError!!) {
            val pop = PopAlertDialog(requireActivity(), lifecycleScope)
            pop.setDataDialog("Ошибка При Ррегистрации Т_Т")
        }
    }

    private fun FragmentLoginBinding.edLoginListener() {
        ETLogin.onFocusChangeListener = View.OnFocusChangeListener { v, hasFocus ->
            if (hasFocus && ETLogin.text.isNullOrBlank())
                ETLogin.setText("@")
        }
        ETLogin.doOnTextChanged { text, start, count, after ->
            TVTitleLogin.text = getString(R.string.login_hint_login)
            TVTitleLogin.setTextColor(ContextCompat.getColor(requireContext(), R.color.grey_500))
            val text: String = ETLogin.text.toString().lowercase()
            if (ETLogin.text.toString() != text) {
                ETLogin.setText(text)
                ETLogin.setSelection(text.length)
            }
            ImageCheckLogin.visibility = View.INVISIBLE
            checkFillingEditTextToLogin()
        }
    }

    private fun FragmentLoginBinding.edPassListener() {
        ETPass.transformationMethod = PasswordTransformationMethod()
        ImageEyePass.setOnClickListener { showPassword() }

        ETPass.doOnTextChanged { text, start, count, after ->
            TVTitlePass.text = getString(R.string.password)
            TVTitlePass.setTextColor(ContextCompat.getColor(requireContext(), R.color.grey_500))
            ImageCheckPass.visibility = View.INVISIBLE
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
            closeKeyBoard()
        }
    }

    private fun closeKeyBoard(){
        val imm: InputMethodManager =
            requireActivity().getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        var view = requireActivity().currentFocus
        if (view == null) view = View(activity)
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    /////////////////////////////////////////////////////////////////

    private fun FragmentLoginBinding.observeLogin() {
        vmAuth.loginResponse.observe(viewLifecycleOwner) {
            when(it) {
                is ApiResponse.Failure -> {
                    val pop = PopAlertDialog(requireActivity(),lifecycleScope)
                    Log.d("ApiAuth","loginResponse falure - " + it.errorMessage)
                    when(it.code){
                        502 -> pop.setDataDialog("Ошибка сервера")
                        else -> pop.setDataDialog("Неверная почта или пароль")
                    }
                }
                ApiResponse.Loading -> {
                    Log.d("ApiAuth","loginResponse Loading")
                }
                is ApiResponse.Success -> {
                    vmToken.saveToken(it.data.token)
                    vm.apiAuthGetUser(vmApi )
                    Log.d("ApiAuth","loginResponse token - " + it.data.token)
                    Log.d("ApiAuth","loginResponse refresh - " + it.data.refresh)
                }
            }
        }
    }

    private fun FragmentLoginBinding.observeUser(){
        vmApi.userInfoResponse.observe(viewLifecycleOwner) {
            when(it) {
                is ApiResponse.Failure -> {
                    Log.d("ApiMain","observeUser falure - " + it.errorMessage)
                    val pop = PopAlertDialog(requireActivity(),lifecycleScope)
                    when(it.code){
                        502 -> pop.setDataDialog("Ошибка сервера")
                        else -> pop.setDataDialog("Неверная почта или пароль")
                    }
                }
                ApiResponse.Loading -> {
                    Log.d("ApiMain","observeUser Loading")
                }
                is ApiResponse.Success -> {
                    Log.d("ApiMain","observeUser info - " + it.data)
                    vm.apply {
                        requireActivity().loginFinish(vmUser,it.data)
                    }.let {
                        findNavController().navigate(R.id.action_navigation_login_to_navigation_home)
                    }
                }
            }
        }
    }
    /////////////////////////////////////////////////////////////////

    private fun FragmentLoginBinding.isValidLogin() =
        ETLogin.text.length in RulesNameAuth.LENGTH_LOGIN_RANGE

    private fun FragmentLoginBinding.isValidPassword() =
        ETPass.text.length in RulesNameAuth.LENGTH_PASSWORD_RANGE

    private fun FragmentLoginBinding.checkFillingEditTextToLogin(){
        when(isValidLogin() && isValidPassword()){
            true -> btnNextStageView(RulesNameAuth.ACTIVE_BUTTON_VIEW)
            else -> btnNextStageView(RulesNameAuth.DEFAULT_BUTTON_VIEW)
        }
    }

    private fun FragmentLoginBinding.initBtnNav() {
        TVBtnResetPassword.setOnClickListener {
            TVBtnResetPassword.timerDoubleButton()
            findNavController().navigate(R.id.action_navigation_login_to_navigation_reset_password_stage_three, null)
        }

        TVBtnLogin.setOnClickListener {
            TVBtnLogin.timerDoubleButton()
            when(isValidLogin() && isValidPassword()){
                true->{
                    btnNextStageView(RulesNameAuth.LOADING_BUTTON_VIEW)
                    vm.postApiAuth(vmAuth,
                        login = ETLogin.text.toString(),
                        pass =  ETPass.text.toString())
                }
                false->{
                    if (isValidLogin()) {
                        TVTitleLogin.text = getString(R.string.please_edit_login_login)
                        TVTitleLogin.setTextColor(ContextCompat.getColor(requireContext(), R.color.yellow))
                    }
                    if (isValidPassword()) {
                        TVTitleLogin.text = getString(R.string.please_edit_password_login)
                        TVTitleLogin.setTextColor(ContextCompat.getColor(requireContext(), R.color.yellow))
                    }
                }
            }
        }

        TVBtnLogInGuest.setOnClickListener {
            TVBtnLogInGuest.timerDoubleButton()
            // Todo DEV guest mode

            val sp = requireActivity().getPreferences(Context.MODE_PRIVATE)
            sp.edit().putBoolean("guest_mode", true).apply()

            Toast.makeText(requireContext(), "Вы вошли как гость", Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_navigation_login_to_navigation_home, null)
        }

        TVBtnRegistration.setOnClickListener {
            TVBtnRegistration.timerDoubleButton()
            findNavController().navigate(R.id.action_navigation_login_to_navigation_registration, null)
        }
    }

    private fun FragmentLoginBinding.initButtonsServices(){
        ImageBtnGoogle.setOnClickListener{
            // todo add fun
        }
        ImageBtnVK.setOnClickListener{
            // todo add fun
        }
        ImageBtnTwitter.setOnClickListener{
            // todo add fun
        }
        ImageBtnShikimori.setOnClickListener{
            // todo add fun
        }
    }

    private fun FragmentLoginBinding.btnNextStageView(state:Int) {
        val context = requireContext()
        when (state) {
            RulesNameAuth.DEFAULT_BUTTON_VIEW -> {
                TVBtnLogin.setTextColor(ContextCompat.getColor(context, R.color.grey_400))
                TVBtnLogin.isClickable = true
                ProgressAuth.visibility = View.GONE
            }
            RulesNameAuth.ACTIVE_BUTTON_VIEW -> {
                TVBtnLogin.setTextColor(ContextCompat.getColor(context, R.color.grey_800))
                TVBtnLogin.isClickable = true
                ProgressAuth.visibility = View.GONE
            }
            RulesNameAuth.LOADING_BUTTON_VIEW -> {
                TVBtnLogin.text = ""
                TVBtnLogin.isClickable = false
                ProgressAuth.visibility = View.VISIBLE
            }
            RulesNameAuth.ERROR_BUTTON_VIEW -> {
                TVBtnLogin.text = getString(R.string.log_in)
                TVBtnLogin.isClickable = true
                ProgressAuth.visibility = View.GONE
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        closeKeyBoard()
    }
}