package ru.android.hikanumaruapp.presentasion.auth.registration.state.one

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.text.HtmlCompat
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import ru.android.hikanumaruapp.presentasion.BaseFragment
import ru.android.hikanumaruapp.R
import ru.android.hikanumaruapp.api.api.token.AuthViewModel
import ru.android.hikanumaruapp.api.init.ApiResponse
import ru.android.hikanumaruapp.databinding.FragmentRegistrationBinding
import ru.android.hikanumaruapp.presentasion.auth.RulesNameAuth
import ru.android.hikanumaruapp.presentasion.auth.registration.RegistrationViewModel
import ru.android.hikanumaruapp.utilits.popdialog.PopAlertDialog


@AndroidEntryPoint
class RegistrationFragment : BaseFragment() {

    private var _binding: FragmentRegistrationBinding? = null
    private val binding get() = _binding!!
    private val vm by activityViewModels<RegistrationViewModel>()

    private val vmAuth by viewModels<AuthViewModel>()

    private var isShowPass: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentRegistrationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            checkFillingEditTextToLogin()

            initBtnNav()
            initBtnClear()

            edEmailListener()
            edPassListener()
            edPassAgainListener()

            observeEmail()
        }
    }

    private fun FragmentRegistrationBinding.edEmailListener() {
        ETEmail.doOnTextChanged { text, start, count, after ->
            ImageCheckMail.apply {
                visibility = View.VISIBLE
                setImageResource(when (isValidEmail()) {
                    true -> R.drawable.ic_sucess_cirlce
                    else -> R.drawable.ic_info_circle_error
                })
            }
            checkFillingEditTextToLogin()
        }
    }

    private fun FragmentRegistrationBinding.edPassListener() {
        ETPassReg.transformationMethod = PasswordTransformationMethod()

        ETPassReg.doOnTextChanged { text, start, count, after ->
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

    private fun FragmentRegistrationBinding.edPassAgainListener() {
        ETPassCheckReg.transformationMethod = PasswordTransformationMethod()

        ETPassCheckReg.doOnTextChanged { text, start, count, after ->
            ImageCheckPassAgain.apply {
                visibility = View.VISIBLE
                setImageResource(when (isValidPasswordAgain()) {
                    true -> R.drawable.ic_sucess_cirlce
                    else -> R.drawable.ic_info_circle_error
                })
            }
            ImageEyePassAgain.setOnClickListener { showPassword() }
            checkFillingEditTextToLogin()
        }
    }

    private fun FragmentRegistrationBinding.showPassword() {
        isShowPass = !isShowPass
        val transformationMethod = when (isShowPass) {
            true -> HideReturnsTransformationMethod.getInstance()
            else -> PasswordTransformationMethod.getInstance()
        }

        ETPassReg.transformationMethod = transformationMethod
        ETPassCheckReg.transformationMethod = transformationMethod
        ImageEyePass.setImageResource(if (isShowPass) R.drawable.ic_eye_show else R.drawable.ic_eye_hide)
        ImageEyePassAgain.setImageResource(if (isShowPass) R.drawable.ic_eye_show else R.drawable.ic_eye_hide)
    }

    private fun FragmentRegistrationBinding.initBtnNav() {
        val htmlString = "Нажимая кнопку «продолжить» \n" +
                "вы принимаете условия <font color=\"#007AFF\">пользовательского соглашения</font>"
        val sequence: CharSequence =
            HtmlCompat.fromHtml(htmlString, HtmlCompat.FROM_HTML_MODE_LEGACY)
        TVUserRequest.text = sequence.toString()

        TVNextStageReg.setOnClickListener {
            TVNextStageReg.timerDoubleButton()
            vm.getCheckEmailStateOne(vmAuth,email = ETEmail.text.toString())
        }

        TVBtnBackReg.setOnClickListener {
            TVNextStageReg.timerDoubleButton()
            findNavController().navigateUp()
        }
    }

    private fun FragmentRegistrationBinding.initBtnClear(){
        ImageBtnClearMail.setOnClickListener{
            ETEmail.text = null
        }
        ImageBtnClearPass.setOnClickListener{
            ETPassReg.text = null
        }
        ImageBtnClearPassAgain.setOnClickListener{
            ETPassCheckReg.text = null
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
    private fun FragmentRegistrationBinding.observeEmail() {
        vmAuth.checkEmailResponse.observe(viewLifecycleOwner) {
            val pop = PopAlertDialog(requireActivity(),lifecycleScope)
            when(it) {
                is ApiResponse.Failure -> {
                    Log.d("ApiAuth","checkEmailResponse falure - " + it.errorMessage)
                    when(it.code){
                        502 -> pop.setDataDialog("${it.code}: Ошибка сервера")
                        else -> pop.setDataDialog("${it.code}: Неверная почта")
                    }
                    btnNextStageView(RulesNameAuth.DEFAULT_BUTTON_VIEW)
                }
                ApiResponse.Loading -> {
                    Log.d("ApiAuth","checkEmailResponse Loading")
                    btnNextStageView(RulesNameAuth.LOADING_BUTTON_VIEW)
                }
                is ApiResponse.Success -> {
                    when(it.data.isAvailable){
                        true-> {
                            changeCheckImage(true)
                            btnNextStageView(RulesNameAuth.ACTIVE_BUTTON_VIEW)
                            vm.apply {
                                vm.setDataStageOne(
                                    email = ETEmail.text.toString(),
                                    pass = ETPassReg.text.toString()
                                )
                            }
                            findNavController().navigate(R.id.action_navigation_registration_to_navigation_registration_two)
                        }
                        false->{
                            pop.setDataDialog("Неверная почта или уже используятся")
                            changeCheckImage(false)
                            btnNextStageView(RulesNameAuth.DEFAULT_BUTTON_VIEW)
                        }
                    }
                }
            }
        }
    }


////////////////////////////////////////////////////////
    private fun FragmentRegistrationBinding.changeCheckImage(check:Boolean,optional:Int=0) {
        ImageCheckMail.visibility = View.VISIBLE
        when (check) {
            true->  ImageCheckMail.setImageResource(R.drawable.ic_sucess_cirlce)
            false-> ImageCheckMail.setImageResource(R.drawable.ic_info_circle_error)
        }
        if (optional == 1)
            ImageCheckMail.setImageResource(R.drawable.ic_load_alt)
    }

    private fun FragmentRegistrationBinding.isValidEmail() =
        Patterns.EMAIL_ADDRESS.matcher(ETEmail.text).matches()

    private fun FragmentRegistrationBinding.isValidPassword() =
        ETPassReg.text.length in RulesNameAuth.LENGTH_PASSWORD_RANGE

    private fun FragmentRegistrationBinding.isValidPasswordAgain() =
        ETPassCheckReg.text.toString() == ETPassReg.text.toString() &&
                ETPassCheckReg.text.toString().length == ETPassReg.text.toString().length


    @SuppressLint("ResourceAsColor")
    fun FragmentRegistrationBinding.checkFillingEditTextToLogin(){
        when(isValidEmail() && isValidPassword() && isValidPasswordAgain()){
            true -> btnNextStageView(RulesNameAuth.ACTIVE_BUTTON_VIEW)
            else -> btnNextStageView(RulesNameAuth.DEFAULT_BUTTON_VIEW)
        }
    }

    private fun FragmentRegistrationBinding.btnNextStageView(state:Int){
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
                // handle error state if needed
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

}