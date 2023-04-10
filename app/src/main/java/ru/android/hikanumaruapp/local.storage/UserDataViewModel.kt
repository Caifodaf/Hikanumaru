package ru.android.hikanumaruapp.local.storage

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.android.hikanumaruapp.api.models.UserRegResponse
import ru.android.hikanumaruapp.model.UserInfo
import javax.inject.Inject

@HiltViewModel
class UserDataViewModel @Inject constructor(
    private val userDataStore: UserDataStore,
): ViewModel() {

    init {}

    internal val user: MutableLiveData<UserInfo> by lazy { MutableLiveData<UserInfo>() }

    fun getUserData() {
        Log.d("padaianf", "3 - " )
        viewModelScope.launch(Dispatchers.IO) {
            userDataStore.getUserData().collect {
                withContext(Dispatchers.Main) {
                    user.value = it
                    Log.d("padaianf", "3 - " + it)
                    Log.d("padaianf", "3 - " +  user.value )
                }
            }
        }
    }

    fun saveUserDataAfterReg(userRegResponse: UserRegResponse) {
        Log.d("padaianf", "2 - " + userRegResponse.toString())
        viewModelScope.launch(Dispatchers.IO) {
            userDataStore.saveUserDataAfterReg(userRegResponse)
        }
    }
//
    //fun deleteToken() {
    //    viewModelScope.launch(Dispatchers.IO) {
    //        userDataStore.deleteToken()
    //    }
    //}

}