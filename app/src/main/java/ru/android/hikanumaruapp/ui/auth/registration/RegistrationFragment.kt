package ru.android.hikanumaruapp.ui.auth.registration

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import ru.android.hikanumaruapp.R
import ru.android.hikanumaruapp.databinding.FragmentRegistrationBinding
import ru.android.hikanumaruapp.utilits.Events
import ru.android.hikanumaruapp.utilits.NavigationFragmentinViewModel
import ru.android.hikanumaruapp.utilits.UIUtils

@AndroidEntryPoint
class RegistrationFragment : Fragment(),UIUtils {

    companion object{
        const val DEFAULT_BUTTON_VIEW = 0
        const val ACTIVE_BUTTON_VIEW = 1
        const val LOADING_BUTTON_VIEW = 2
        const val ERROR_BUTTON_VIEW = 3
    }

    private var _binding: FragmentRegistrationBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<RegistrationViewModel>()

    private var lReqPass: Boolean  = false
    private var lReqChPass: Boolean  = false
    private var lReqMail: Boolean  = false

    private var isShowPass: Boolean = false
    private var isCheckMail: Boolean = false

    private val navigationEventsObserver = Events.EventObserver { event ->
        when (event) {
            is NavigationFragmentinViewModel.NavigationFrag -> navigateNav(event.navigation, event.bundle)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRegistrationBinding.inflate(inflater, container, false)
        val root: View = binding.root

        initUI()

        return root
    }

    private fun initUI() {

        initBtnNav()
        initBtnClear()

        edEmailListener()
        edPassListener()
        edPassAgainListener()

        observeEmail()
        observeError()
    }

    private fun edEmailListener() {
        binding.etEmail.doOnTextChanged { text, start, count, after ->
            binding.ivCheckMail.visibility = View.VISIBLE
            lReqMail = Patterns.EMAIL_ADDRESS.matcher(binding.etEmail.text.toString()).matches()

            when (lReqPass) {
                true ->  binding.ivCheckMail.setImageResource(R.drawable.ic_sucess_cirlce)
                false ->  binding.ivCheckMail.setImageResource(R.drawable.ic_info_circle_error)
            }
            checkFillingEditTextToLogin()
        }
    }

    private fun edPassListener() {
        binding.etPassReg.transformationMethod = PasswordTransformationMethod()

        binding.etPassReg.doOnTextChanged { text, start, count, after ->
            binding.ivEyePass.visibility = View.VISIBLE
            binding.ivCheckPass.visibility = View.VISIBLE
            binding.ivEyePass.setOnClickListener {
                showPassword()
            }
            lReqPass = binding.etPassReg.text.toString().length in 5..40

            when (lReqPass) {
                true ->  binding.ivCheckPass.setImageResource(R.drawable.ic_sucess_cirlce)
                false ->  binding.ivCheckPass.setImageResource(R.drawable.ic_info_circle_error)
            }
            checkFillingEditTextToLogin()
        }
    }

    private fun edPassAgainListener() {
        binding.etPassCheckReg.transformationMethod = PasswordTransformationMethod()

        binding.etPassCheckReg.doOnTextChanged { text, start, count, after ->
            binding.ivEyePass.visibility = View.VISIBLE
            binding.ivCheckPass.visibility = View.VISIBLE
            binding.ivEyePass.setOnClickListener {
                showPassword()
            }
            lReqChPass = binding.etPassCheckReg.text == binding.etPassReg.text

            when (lReqChPass) {
                true ->  binding.ivCheckPass.setImageResource(R.drawable.ic_sucess_cirlce)
                false ->  binding.ivCheckPass.setImageResource(R.drawable.ic_info_circle_error)
            }
            checkFillingEditTextToLogin()
        }
    }

    private fun showPassword() {
        when(isShowPass) {
            true->{
                isShowPass=false
                binding.etPassReg.transformationMethod = PasswordTransformationMethod()
                binding.etPassCheckReg.transformationMethod = PasswordTransformationMethod()
                binding.ivEyePass.setImageResource(R.drawable.ic_eye_hide)
                binding.ivEyePassAgain.setImageResource(R.drawable.ic_eye_hide)
            }
            false->{
                isShowPass=true
                binding.ivEyePass.setImageResource(R.drawable.ic_eye_show)
                binding.ivEyePassAgain.setImageResource(R.drawable.ic_eye_show)
                binding.etPassReg.transformationMethod = HideReturnsTransformationMethod.getInstance()
                binding.etPassCheckReg.transformationMethod = HideReturnsTransformationMethod.getInstance()
            }
        }
    }

    @SuppressLint("ResourceAsColor")
    fun checkFillingEditTextToLogin(){
        if(lReqMail && lReqPass && lReqChPass)
            btnNextStageView(ACTIVE_BUTTON_VIEW)
        else
            btnNextStageView(DEFAULT_BUTTON_VIEW)
    }

    private fun initBtnNav() {
        binding.tvNextStageReg.setOnClickListener {
            timerDoubleBtn(binding.tvNextStageReg,5000)
            btnNextStageView(LOADING_BUTTON_VIEW)

            viewModel.postCheckEmail(
                email = binding.etEmail.text.toString(),
                pass = binding.etPassReg.text.toString()
            )
        }

        binding.tvBtnBackReg.setOnClickListener {
            timerDoubleBtn(binding.tvBtnBackReg)
            requireActivity().supportFragmentManager.popBackStack()
        }
    }

    private fun observeEmail(){
        viewModel.emailResponse.observe(viewLifecycleOwner, Observer { response ->
            when(response.success){
                true->{
                    changeCheckImage(true)
                    btnNextStageView(ACTIVE_BUTTON_VIEW)

                    findNavController().navigate(R.id.navigation_registration_two, null)
                }
                false->{
                    // todo disable pop
                    //errorPop(response.body()!!.message,errorLayout,inflater)
                    changeCheckImage(false)
                    btnNextStageView(DEFAULT_BUTTON_VIEW)

                }
            }
        })
    }

    private fun observeError(){
        viewModel.error.observe(viewLifecycleOwner, Observer { error ->
                //todo add error
        })
    }

    private fun changeCheckImage(check:Boolean,optional:Int=0) {
        binding.ivCheckMail.visibility = View.VISIBLE
        when (check) {
            true->binding.ivCheckMail.setImageResource(R.drawable.ic_sucess_cirlce)
            false->binding.ivCheckMail.setImageResource(R.drawable.ic_info_circle_error)
        }
        if (optional == 1)
            binding.ivCheckMail.setImageResource(R.drawable.ic_load_alt)
    }

    private fun btnNextStageView(state:Int){
        when (state){
            DEFAULT_BUTTON_VIEW -> {
                binding.progressAuthBtn.visibility = View.GONE
                binding.tvNextStageReg.text = getString(R.string.proceed)
                binding.tvNextStageReg.setTextColor(resources.getColor(R.color.grey_light_alternative_4));
                DrawableCompat.setTint(binding.tvNextStageReg.background, ContextCompat.getColor(requireContext(), R.color.grey_light_alternative_1));
                binding.tvNextStageReg.isClickable = false
            }
            ACTIVE_BUTTON_VIEW -> {
                binding.progressAuthBtn.visibility = View.GONE
                binding.tvNextStageReg.text = getString(R.string.proceed)
                binding.tvNextStageReg.setTextColor(resources.getColor(R.color.white));
                DrawableCompat.setTint(binding.tvNextStageReg.background, ContextCompat.getColor(requireContext(), R.color.blue));
                binding.tvNextStageReg.isClickable = true
            }
            LOADING_BUTTON_VIEW -> {
                binding.progressAuthBtn.visibility = View.VISIBLE
                binding.tvNextStageReg.text = ""
                binding.progressAuthBtn.isClickable = false
            }
            ERROR_BUTTON_VIEW -> {

            }
        }
    }

    private fun initBtnClear(){
        binding.ivBtnClearMail.setOnClickListener{
            binding.etEmail.text = null
        }
        binding.ivBtnClearPass.setOnClickListener{
            binding.etPassReg.text = null
        }
        binding.ivBtnClearPassAgain.setOnClickListener{
            binding.etPassCheckReg.text = null
        }
    }

    fun navigateNav(navigation: Int, bundle: Bundle?) {
        findNavController().navigate(navigation, bundle)
    }


}