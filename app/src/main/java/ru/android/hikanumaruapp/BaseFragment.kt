package ru.android.hikanumaruapp

import android.annotation.SuppressLint
import android.os.Handler
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment

abstract class BaseFragment: Fragment() {

    private var isBack = false
    private lateinit var isBackToast: Toast

    protected fun btnBack() {
        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                @SuppressLint("WrongConstant")
                override fun handleOnBackPressed() {
                    if (isBack) {
                        isBackToast.cancel()
                        ActivityCompat.finishAffinity(requireActivity())
                    } else {
                        isBackToast =
                            Toast.makeText(requireContext(), "Нажмите еще раз для выхода", 500)
                        isBackToast.show()
                        checkTimeExit()
                    }
                }
            })
    }

    private fun checkTimeExit(){
        isBack = true
        Handler().postDelayed({
            isBack = false
        },1000)
    }
}