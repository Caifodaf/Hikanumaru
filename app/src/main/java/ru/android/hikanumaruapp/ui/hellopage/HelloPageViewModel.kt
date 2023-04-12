package ru.android.hikanumaruapp.ui.hellopage

import android.content.Context
import android.content.SharedPreferences
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import ru.android.hikanumaruapp.ui.auth.login.LoginViewModel
import javax.inject.Inject

@HiltViewModel
class HelloPageViewModel @Inject constructor(): ViewModel() {

    companion object{
        const val PK_FIRST_LAUNCH = "pref_first_launch"
        const val PH_LOGIN_AUTH = "pref_login_auth"
        const val MAIN = 2
        const val LOGIN = 1
        const val START = 0
    }

    fun FragmentActivity.onStartup() =
        getPreferences(Context.MODE_PRIVATE).getBoolean(PK_FIRST_LAUNCH, true)

}