package ru.android.hikanumaruapp.presentasion.auth.reset.password

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import ru.android.hikanumaruapp.R
import ru.android.hikanumaruapp.api.api.token.AuthViewModel
import ru.android.hikanumaruapp.databinding.FragmentResetPasswordStageOneBinding
import ru.android.hikanumaruapp.presentasion.auth.RulesNameAuth
import ru.android.hikanumaruapp.utilits.UIUtils


@AndroidEntryPoint
class ResetPasswordStageOneFragment : Fragment(),UIUtils {

    private var _binding: FragmentResetPasswordStageOneBinding? = null
    private val binding get() = _binding!!
    private val vm by viewModels<ResetPasswordViewModel>()

    private val vmAuth by viewModels<AuthViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentResetPasswordStageOneBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            initBtnNav()
            initBtnClear()
            edEmailListener()
        }
    }

    private fun FragmentResetPasswordStageOneBinding.edEmailListener() {
        ETEmail.doOnTextChanged { text, start, count, after ->
            TVTitleEmail.text = getString(R.string.email)
            ImageCheckMail.apply {
                visibility = View.VISIBLE
                setImageResource(when (isValidEmail()) {
                    true -> R.drawable.ic_sucess_cirlce
                    else -> R.drawable.ic_info_circle_error
                })
            }
            checkFillingEditText()
        }
    }

    private fun FragmentResetPasswordStageOneBinding.isValidEmail() =
        Patterns.EMAIL_ADDRESS.matcher(ETEmail.text).matches()

    @SuppressLint("ResourceAsColor")
    fun FragmentResetPasswordStageOneBinding.checkFillingEditText() {
        when (isValidEmail()) {
            true -> btnNextStageView(RulesNameAuth.ACTIVE_BUTTON_VIEW)
            else -> btnNextStageView(RulesNameAuth.DEFAULT_BUTTON_VIEW)
        }
    }

    private fun FragmentResetPasswordStageOneBinding.initBtnClear() {
        ImageBtnClearMail.setOnClickListener {
            ETEmail.text = null
        }
    }

    private fun FragmentResetPasswordStageOneBinding.initBtnNav() {
        TVBtnBack.setOnClickListener {
            TVBtnBack.timerDoubleBtn()
            findNavController().navigateUp()
        }

        TVNextStageRP.setOnClickListener {
            TVNextStageRP.timerDoubleBtn()
            when (isValidEmail()) {
                true -> {
                    btnNextStageView(RulesNameAuth.LOADING_BUTTON_VIEW)
                    //TODO POST CHECK EMAIL
//                    vm.postApiAuth(vmAuth,
//                        login = ETLogin.text.toString(),
//                        pass = ETPass.text.toString())
                }
                false -> {
                    if (isValidEmail()) {
                        TVTitleEmail.text = getString(R.string.please_edit_email_password)
                        TVTitleEmail.setTextColor(ContextCompat.getColor(requireContext(),
                            R.color.yellow))
                    }
                }
            }
        }
    }

    private fun FragmentResetPasswordStageOneBinding.btnNextStageView(state: Int) {
        val context = requireContext()
        when (state) {
            RulesNameAuth.DEFAULT_BUTTON_VIEW -> {
                TVNextStageRP.setTextColor(ContextCompat.getColor(context, R.color.grey_400))
                TVNextStageRP.isClickable = true
                ProgressAuth.visibility = View.GONE
            }
            RulesNameAuth.ACTIVE_BUTTON_VIEW -> {
                TVNextStageRP.setTextColor(ContextCompat.getColor(context, R.color.grey_800))
                TVNextStageRP.isClickable = true
                ProgressAuth.visibility = View.GONE
            }
            RulesNameAuth.LOADING_BUTTON_VIEW -> {
                TVNextStageRP.text = ""
                TVNextStageRP.isClickable = false
                ProgressAuth.visibility = View.VISIBLE
            }
            RulesNameAuth.ERROR_BUTTON_VIEW -> {
                TVNextStageRP.text = getString(R.string.contines)
                TVNextStageRP.isClickable = true
                ProgressAuth.visibility = View.GONE
            }
        }
    }

    private fun View.timerDoubleBtn(time: Long = 2000) {
        isClickable = false
        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed({
            isClickable = true
        }, time)
    }

    private fun closeKeyBoard() {
        val imm: InputMethodManager =
            requireActivity().getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        var view = requireActivity().currentFocus
        if (view == null) view = View(activity)
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        closeKeyBoard()
    }

}
