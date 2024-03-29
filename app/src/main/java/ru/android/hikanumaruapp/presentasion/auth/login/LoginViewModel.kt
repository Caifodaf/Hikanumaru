package ru.android.hikanumaruapp.presentasion.auth.login

import android.content.Context
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import ru.android.hikanumaruapp.api.api.main.MainApiViewModel
import ru.android.hikanumaruapp.api.api.token.AuthViewModel
import ru.android.hikanumaruapp.api.init.CoroutinesErrorHandler
import ru.android.hikanumaruapp.api.models.ErrorResponse
import ru.android.hikanumaruapp.api.models.UserAuthPost
import ru.android.hikanumaruapp.api.models.UserRegResponse
import ru.android.hikanumaruapp.data.local.user.UserDataViewModel
import ru.android.hikanumaruapp.presentasion.hellopage.HelloPageViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor() : ViewModel() {

    private lateinit var job: Job

    val error: MutableLiveData<ErrorResponse> by lazy { MutableLiveData<ErrorResponse>() }

    internal fun postApiAuth(vmAuth: AuthViewModel,login: String, pass: String) {
        vmAuth.postLogin(UserAuthPost(login, pass),
            object: CoroutinesErrorHandler {
            override fun onError(cause: Throwable?, message: String) {
                when(cause.toString().substringBefore(':')){
                    "java.net.SocketTimeoutException" -> error.postValue(ErrorResponse(504, message.toString()))
                    "java.net.UnknownHostException"  ->  error.postValue(ErrorResponse(-1, message.toString()))
                    else  ->  error.postValue(ErrorResponse(502, message.toString()))
                }
            }
        })
    }

    internal fun apiAuthGetUser(vmApi: MainApiViewModel) {
        vmApi.getUserInfo(
            object: CoroutinesErrorHandler {
                override fun onError(cause: Throwable?, message: String) {
                    when(cause.toString().substringBefore(':')){
                        "java.net.SocketTimeoutException" -> error.postValue(ErrorResponse(504, message.toString()))
                        "java.net.UnknownHostException"  ->  error.postValue(ErrorResponse(-1, message.toString()))
                        else  ->  error.postValue(ErrorResponse(502, message.toString()))
                    }
                }
            })
    }

    internal fun FragmentActivity.loginFinish(vmUser: UserDataViewModel, data: UserRegResponse) {
        vmUser.apply{
            createUserDataAfterReg(data)
            changeVMModeGuest(false)
        }.let {
            onStartupEdit()
        }
    }

    private fun FragmentActivity.onStartupEdit() {
        getPreferences(Context.MODE_PRIVATE)
            .edit()
            .putBoolean(HelloPageViewModel.PK_FIRST_LAUNCH, false)
            .apply()
    }

    override fun onCleared() {
        if(::job.isInitialized) job.cancel()
        super.onCleared()
    }
}