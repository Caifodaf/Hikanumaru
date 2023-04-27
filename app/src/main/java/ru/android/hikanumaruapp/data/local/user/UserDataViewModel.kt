package ru.android.hikanumaruapp.data.local.user

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.android.hikanumaruapp.api.models.UserRegResponse
import ru.android.hikanumaruapp.data.model.UserInfo
import javax.inject.Inject

@HiltViewModel
class UserDataViewModel @Inject constructor(): ViewModel(),UserSharedPreferenceAdapter {

    internal val user: MutableLiveData<UserInfo> by lazy { MutableLiveData<UserInfo>() }

    fun Context.getUserData() {
        viewModelScope.launch(Dispatchers.IO) {
            applicationContext.getUserInfo().collect {
                withContext(Dispatchers.Main) {
                    user.value = it
                }
            }
        }
    }

    fun Context.createUserDataAfterReg(userRegResponse: UserRegResponse) {
        viewModelScope.launch(Dispatchers.IO) {
            applicationContext.createUserReg(userRegResponse)
        }
    }

    fun Context.changeVMModeGuest(isGuest: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            applicationContext.changeModeGuest(isGuest)
        }
    }

}