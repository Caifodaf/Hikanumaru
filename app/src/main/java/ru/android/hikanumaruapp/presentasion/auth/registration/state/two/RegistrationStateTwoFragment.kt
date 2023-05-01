package ru.android.hikanumaruapp.presentasion.auth.registration.state.two

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnFocusChangeListener
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import ru.android.hikanumaruapp.presentasion.BaseFragment
import ru.android.hikanumaruapp.R
import ru.android.hikanumaruapp.api.api.main.MainApiViewModel
import ru.android.hikanumaruapp.api.api.token.AuthViewModel
import ru.android.hikanumaruapp.api.init.ApiResponse
import ru.android.hikanumaruapp.api.token.TokenViewModel
import ru.android.hikanumaruapp.databinding.FragmentRegistrationStateTwoBinding
import ru.android.hikanumaruapp.data.local.user.UserDataViewModel
import ru.android.hikanumaruapp.presentasion.auth.RulesNameAuth
import ru.android.hikanumaruapp.presentasion.auth.registration.RegistrationViewModel
import ru.android.hikanumaruapp.utilits.popdialog.PopAlertDialog

@AndroidEntryPoint
class RegistrationStateTwoFragment() : BaseFragment() {

    companion object{
        const val BUNDLE_ERROR_CODE = "error_reg"
    }

    private var _binding: FragmentRegistrationStateTwoBinding? = null
    private val binding get() = _binding!!
    private val vm by activityViewModels<RegistrationViewModel>()

    private val vmAuth by viewModels<AuthViewModel>()
    private val vmToken by activityViewModels<TokenViewModel>()
    private val vmApi by viewModels<MainApiViewModel>()

    private val vmUser by viewModels<UserDataViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentRegistrationStateTwoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            checkFillingEditTextToLogin()

            initBtnNav()
            initBtnClear()

            edLoginListener()
            edNameListener()

            observeLogin()
            observeErrors()
        }
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
            vm.getCheckLoginStateTwo(vmAuth, login = ETLogin.text.toString())
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
    private fun FragmentRegistrationStateTwoBinding.observeErrors() {
        vm.error.observe(viewLifecycleOwner, Observer { error->
            val pop = PopAlertDialog(requireActivity(),lifecycleScope)
            when(error.code){
                502 -> pop.setDataDialog("Ошибка сервера")
                504 -> pop.setDataDialog("Время ожидания первышенно")
                -1 -> pop.setDataDialog("Нестабильная работа сети")
                else -> pop.setDataDialog("Неверная почта или пароль")
            }
            btnNextStageView(RulesNameAuth.DEFAULT_BUTTON_VIEW)
        })
    }

    private fun FragmentRegistrationStateTwoBinding.observeLogin() {
        vmAuth.checkLoginResponse.observe(viewLifecycleOwner) {
            val pop = PopAlertDialog(requireActivity(), lifecycleScope)
            when (it) {
                is ApiResponse.Failure -> {
                    Log.d("ApiAuth", "checkLoginResponse falure - " + it.errorMessage)
                    when (it.code) {
                        502 -> pop.setDataDialog("${it.code}: Ошибка сервера")
                        else -> pop.setDataDialog("${it.code}: Неверный логин")
                    }
                    btnNextStageView(RulesNameAuth.DEFAULT_BUTTON_VIEW)
                }
                ApiResponse.Loading -> {
                    Log.d("ApiAuth", "checkLoginResponse Loading")
                    btnNextStageView(RulesNameAuth.LOADING_BUTTON_VIEW)
                }
                is ApiResponse.Success -> {
                    when (it.data.isAvailable) {
                        true -> {
                            changeCheckImage(true)
                            vm.apply {
                                vm.apply {
                                    val login = ETLogin.text.toString().substring(1)
                                    setDataStageTwo(
                                        login = login,
                                        userName = ETNameReg.text.toString()
                                    )
                                    postApiCreateUser(vmAuth)
                                }
                            }
                        }
                        false -> {
                            pop.setDataDialog("Неверный логин или уже используятся")
                            changeCheckImage(false)
                            btnNextStageView(RulesNameAuth.DEFAULT_BUTTON_VIEW)
                        }
                    }
                }
            }
        }

        vmAuth.createUserResponse.observe(viewLifecycleOwner) {
            val pop = PopAlertDialog(requireActivity(), lifecycleScope)
            when (it) {
                is ApiResponse.Failure -> {
                    Log.d("ApiAuth", "createUserResponse falure - " + it.errorMessage)
                    when (it.code) {
                        502 -> pop.setDataDialog("${it.code}: Ошибка сервера")
                        else -> pop.setDataDialog("${it.code}: Ошибка Регистрации")
                    }
                    btnNextStageView(RulesNameAuth.DEFAULT_BUTTON_VIEW)
                    errorCreateUser()
                }
                ApiResponse.Loading -> {
                    Log.d("ApiAuth", "createUserResponse Loading")
                    btnNextStageView(RulesNameAuth.LOADING_BUTTON_VIEW)
                }
                is ApiResponse.Success -> {
                    Log.d("vmToken","token - " + it.data.token)
                    Log.d("vmToken","refresh - " + it.data.refresh)
                    vm.apiAuthGetUser(vmApi)
                    vmToken.saveToken(it.data)
                }
            }
        }

        vmApi.userInfoResponse.observe(viewLifecycleOwner) {
            val pop = PopAlertDialog(requireActivity(), lifecycleScope)
            when (it) {
                is ApiResponse.Failure -> {
                    Log.d("ApiAuth", "userInfoResponse failure - " + it.errorMessage)
                     when (it.code) {
                        502 -> pop.setDataDialog("Ошибка сервера")
                        else -> pop.setDataDialog("Ошибка Регистрации")
                    }
                    errorCreateUser()
                }
                ApiResponse.Loading -> {
                    Log.d("ApiAuth", "userInfoResponse Loading")
                    btnNextStageView(RulesNameAuth.LOADING_BUTTON_VIEW)
                }
                is ApiResponse.Success -> {
                    Log.d("vmApi User","User - " + it.data)
                    vm.apply {
                        requireActivity().registrationFinish(vmUser,it.data)
                    }.let {
                        findNavController().navigate(R.id.action_navigation_registration_two_to_navigation_home)
                    }
                }
            }
        }
    }

    private fun errorCreateUser(){
        vmToken.deleteToken()

        val transaction: FragmentTransaction = parentFragmentManager.beginTransaction()
        val bundle = Bundle()
        bundle.putBoolean(BUNDLE_ERROR_CODE,true)
        findNavController().navigate(R.id.action_navigation_registration_two_to_navigation_login, bundle)
        transaction.remove(this@RegistrationStateTwoFragment)
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE)
        transaction.commit()
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
                TVNextStageReg.isClickable = false
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

    override fun onDestroyView() {
        super.onDestroyView()
    }
}