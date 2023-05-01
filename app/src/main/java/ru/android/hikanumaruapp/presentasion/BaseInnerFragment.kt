package ru.android.hikanumaruapp.presentasion

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import ru.android.hikanumaruapp.presentasion.mangapage.MangaPageViewModel
import ru.android.hikanumaruapp.utilits.UIUtils

abstract class BaseInnerFragment: Fragment(),UIUtils {

    protected fun setupOnBackPressed() {
        activity?.onBackPressedDispatcher?.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    findNavController().navigateUp()
                }
            })
    }
}