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
    private var doubleBackToExitPressedOnce = false

    protected fun setupOnBackPressed() {
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                @SuppressLint("WrongConstant")
                override fun handleOnBackPressed() {
                    if (doubleBackToExitPressedOnce) {
                        isBackToast.cancel()
                        ActivityCompat.finishAffinity(requireActivity())
                    } else {
                        doubleBackToExitPressedOnce = true
                        isBackToast =
                            Toast.makeText(requireContext(), "Нажмите еще раз для выхода", 500)
                        isBackToast.show()
                        view?.postDelayed({
                            doubleBackToExitPressedOnce = false
                        }, 1000)
                    }
                }
            })
    }
}