package ru.android.hikanumaruapp.ui.auth.registration.state.two

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnFocusChangeListener
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import ru.android.hikanumaruapp.R
import ru.android.hikanumaruapp.databinding.FragmentRegistrationStateTwoBinding
import ru.android.hikanumaruapp.ui.auth.registration.RegistrationFragment
import ru.android.hikanumaruapp.ui.auth.registration.RegistrationViewModel
import ru.android.hikanumaruapp.utilits.navigation.Events
import ru.android.hikanumaruapp.utilits.navigation.NavigationFragmentinViewModel
import ru.android.hikanumaruapp.utilits.UIUtils


class RegistrationStateTwoFragment : Fragment(), UIUtils {

    companion object{
        const val DEFAULT_BUTTON_VIEW = 0
        const val ACTIVE_BUTTON_VIEW = 1
        const val LOADING_BUTTON_VIEW = 2
        const val ERROR_BUTTON_VIEW = 3
    }

    private var _binding: FragmentRegistrationStateTwoBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<RegistrationViewModel>()

    private var lReqName: Boolean  = false
    private var lReqLogin: Boolean  = false

    private val navigationEventsObserver = Events.EventObserver { event ->
        when (event) {
            is NavigationFragmentinViewModel.NavigationFrag -> navigateNav(event.navigation, event.bundle)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRegistrationStateTwoBinding.inflate(inflater, container, false)
        val root: View = binding.root

        initUI()


        return root
    }

    private fun initUI() {

        initBtnNav()
        initBtnClear()

        edLoginListener()
        edNameListener()

        observeError()
        observeLogin()
    }

    private fun edLoginListener() {
        binding.etLoginReg.doOnTextChanged { text, start, count, after ->
            binding.ivCheckLogin.visibility = View.VISIBLE
            lReqLogin = binding.etLoginReg.text.toString().length in 3..30
            when (lReqName) {
                true -> binding.ivCheckLogin.setImageResource(R.drawable.ic_sucess_cirlce)
                false -> binding.ivCheckLogin.setImageResource(R.drawable.ic_info_circle_error)
            }
            checkFillingEditTextToLogin()
        }
    }

    private fun edNameListener() {
        binding.etNameReg.onFocusChangeListener = OnFocusChangeListener { v, hasFocus ->
            if (hasFocus && binding.etNameReg.text.isNullOrBlank())
                binding.etNameReg.setText("@")
        }

        binding.etNameReg.doOnTextChanged { text, start, count, after ->
            binding.ivCheckName.visibility = View.VISIBLE
            lReqLogin = binding.etNameReg.text.toString().length in 3..30
            when (lReqName) {
                true -> binding.ivCheckName.setImageResource(R.drawable.ic_sucess_cirlce)
                false -> binding.ivCheckName.setImageResource(R.drawable.ic_info_circle_error)
            }
            checkFillingEditTextToLogin()
        }
    }

    @SuppressLint("ResourceAsColor")
    fun checkFillingEditTextToLogin(){
        if(lReqName && lReqLogin)
            btnNextStageView(ACTIVE_BUTTON_VIEW)
         else
            btnNextStageView(DEFAULT_BUTTON_VIEW)
    }

    private fun observeError(){
        viewModel.error.observe(viewLifecycleOwner, Observer { error ->
            val transaction: FragmentTransaction = parentFragmentManager.beginTransaction()
            val bundle = Bundle()

            bundle.putString("error","Ошибка при регистрации.")

            findNavController().navigate(R.id.navigation_start_page, bundle)
            transaction.remove(this)
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE)
            transaction.commit()
        })
    }

    private fun observeLogin(){
        viewModel.emailResponse.observe(viewLifecycleOwner, Observer { response ->
            when(response.success){
                true->{
                    changeCheckImage(true)
                    viewModel.postCreateUser(requireActivity())
                    //btnNextStageView(RegistrationFragment.ACTIVE_BUTTON_VIEW)
                    //findNavController().navigate(R.id.navigation_registration_two, null)
                }
                false->{
                    // todo disable pop
                    //errorPop(response.body()!!.message,errorLayout,inflater)
                    changeCheckImage(false)
                    btnNextStageView(RegistrationFragment.DEFAULT_BUTTON_VIEW)
                }
            }
        })
    }

    private fun initBtnNav() {
        binding.tvNextStageReg.setOnClickListener {
            timerDoubleBtn(binding.tvNextStageReg,5000)
            btnNextStageView(RegistrationFragment.LOADING_BUTTON_VIEW)

            viewModel.apiCheckLogin(
                login = binding.etLoginReg.text.toString(),
                userName = binding.etNameReg.text.toString()
                )
        }

        binding.tvBtnBackReg.setOnClickListener {
            timerDoubleBtn(binding.tvBtnBackReg)
            requireActivity().supportFragmentManager.popBackStack()
        }
    }

    private fun initBtnClear(){
        binding.ivBtnClearLogin.setOnClickListener{
            binding.etNameReg.setText("@")
        }
        binding.ivBtnClearName.setOnClickListener{
            binding.etNameReg.text = null
        }
    }

    private fun changeCheckImage(check:Boolean,optional:Int=0) {
        binding.ivCheckLogin.visibility = View.VISIBLE
        when (check) {
            true->binding.ivCheckLogin.setImageResource(R.drawable.ic_sucess_cirlce)
            false->binding.ivCheckLogin.setImageResource(R.drawable.ic_info_circle_error)
        }
        if (optional == 1)
            binding.ivCheckLogin.setImageResource(R.drawable.ic_load_alt)
    }

    private fun btnNextStageView(state:Int){
        when (state){
            RegistrationFragment.DEFAULT_BUTTON_VIEW -> {
                binding.progressAuthBtn.visibility = View.GONE
                binding.tvNextStageReg.text = getString(R.string.proceed)
                binding.tvNextStageReg.setTextColor(resources.getColor(R.color.grey_light_alternative_4));
                DrawableCompat.setTint(binding.tvNextStageReg.background, ContextCompat.getColor(requireContext(), R.color.grey_light_alternative_1));
                binding.tvNextStageReg.isClickable = false
            }
            RegistrationFragment.ACTIVE_BUTTON_VIEW -> {
                binding.progressAuthBtn.visibility = View.GONE
                binding.tvNextStageReg.text = getString(R.string.proceed)
                binding.tvNextStageReg.setTextColor(resources.getColor(R.color.white));
                DrawableCompat.setTint(binding.tvNextStageReg.background, ContextCompat.getColor(requireContext(), R.color.blue));
                binding.tvNextStageReg.isClickable = true
            }
            RegistrationFragment.LOADING_BUTTON_VIEW -> {
                binding.progressAuthBtn.visibility = View.VISIBLE
                binding.tvNextStageReg.text = ""
                binding.progressAuthBtn.isClickable = false
            }
            RegistrationFragment.ERROR_BUTTON_VIEW -> {

            }
        }
    }

    fun navigateNav(navigation: Int, bundle: Bundle?) {
        findNavController().navigate(navigation, bundle)
    }

}