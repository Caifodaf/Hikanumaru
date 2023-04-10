package ru.android.hikanumaruapp.ui.auth.login

import android.content.Context
import android.util.Log
import androidx.datastore.dataStore
import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import ru.android.hikanumaruapp.api.api.main.MainApiViewModel
import ru.android.hikanumaruapp.api.api.token.AuthViewModel
import ru.android.hikanumaruapp.api.init.CoroutinesErrorHandler
import ru.android.hikanumaruapp.api.models.ErrorResponse
import ru.android.hikanumaruapp.api.models.UserAuthPost
import ru.android.hikanumaruapp.api.models.UserRegResponse
import ru.android.hikanumaruapp.local.storage.SharedPreferenceAdapter
import ru.android.hikanumaruapp.local.storage.UserDataConst
import ru.android.hikanumaruapp.local.storage.UserDataSingletonModule
import ru.android.hikanumaruapp.local.storage.UserDataViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor() : ViewModel(), SharedPreferenceAdapter {

    companion object{
        const val PK_FIRST_LAUNCH = "pref_first_launch"
        const val PH_LOGIN_AUTH = "pref_login_auth"
    }

    private lateinit var job: Job

    internal val error: MutableLiveData<ErrorResponse> by lazy { MutableLiveData<ErrorResponse>() }

    //private val Context.dataStore by preferencesDataStore(name = UserDataConst.USER_PREFERENCES_NAME)
    //private val Context.userPreferencesStore by dataStore(
    //    fileName = UserDataConst.DATA_STORE_FILE_NAME,
    //    serializer = UserPreferencesSerializer
    //)

    internal fun postApiAuth(vmAuth: AuthViewModel,login: String, pass: String) {
        //vmAuth.postLogin(UserAuthPost("McLOVIN", "80082"), //TODO DEBUG
        vmAuth.postLogin(UserAuthPost(login, pass),
            object: CoroutinesErrorHandler {
            override fun onError(message: String) {
                // TODO ERROR
                //_error.postValue(ErrorResponse(1, message.toString()))
            }
        })
    }

    internal fun apiAuthGetUser(vmApi: MainApiViewModel) {
        vmApi.getUserInfo(
            object: CoroutinesErrorHandler {
                override fun onError(message: String) {
                    // TODO ERROR
                    //_error.postValue(ErrorResponse(1, message.toString()))
                }
            })
    }

    fun setupUserData(vmUser: UserDataViewModel,userRegResponse: UserRegResponse) {
        Log.d("padaianf", "1 - " + userRegResponse.toString())
        vmUser.saveUserDataAfterReg(userRegResponse)
        vmUser.getUserData()
    }

    override fun onCleared() {
        if(::job.isInitialized) job.cancel()
        super.onCleared()
    }
}