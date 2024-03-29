package ru.android.hikanumaruapp.presentasion.auth.registration

import android.content.Context
import android.util.Log
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import ru.android.hikanumaruapp.api.api.main.MainApiViewModel
import ru.android.hikanumaruapp.api.api.token.AuthViewModel
import ru.android.hikanumaruapp.api.init.CoroutinesErrorHandler
import ru.android.hikanumaruapp.api.models.*
import ru.android.hikanumaruapp.data.local.user.UserDataViewModel
import ru.android.hikanumaruapp.presentasion.hellopage.HelloPageViewModel
import javax.inject.Inject


@HiltViewModel
class RegistrationViewModel @Inject constructor() : ViewModel(){

    private lateinit var job: Job

    val error: MutableLiveData<ErrorResponse> by lazy { MutableLiveData<ErrorResponse>() }

    private var email: String = ""
    private var pass: String = ""
    private var login: String = ""
    private var userName: String = ""

    private var isCheckMail: Boolean = false
    private var checkMail: String = ""

    internal fun setDataStageOne(email: String, pass: String) {
        this.email = email
        this.pass = pass
    }

    internal fun setDataStageTwo(login: String, userName: String) {
        this.login = login
        this.userName = userName
    }

    internal fun getCheckEmailStateOne(vmAuth: AuthViewModel,email: String) {
        // TODO Fasf
        //if (checkMail == email && email.isNotBlank() && isCheckMail) {
        //    vmAuth.checkEmailResponse.value = email
        //} else {
        //  setDataStageOne(email, pass)
        // val emailLoad = email
        vmAuth.getCheckMail(post = email,
            object : CoroutinesErrorHandler {
                override fun onError(cause: Throwable?, message: String) {
                    when(cause.toString().substringBefore(':')){
                        "java.net.SocketTimeoutException" -> error.postValue(ErrorResponse(504, message.toString()))
                        "java.net.UnknownHostException"  ->  error.postValue(ErrorResponse(-1, message.toString()))
                        else  ->  error.postValue(ErrorResponse(502, message.toString()))
                    }
                }
            })
    }

    internal fun getCheckLoginStateTwo(vmAuth: AuthViewModel,login: String) {
        vmAuth.getCheckLogin(post = login,
            object : CoroutinesErrorHandler {
                override fun onError(cause: Throwable?, message: String) {
                    when(cause.toString().substringBefore(':')){
                        "java.net.SocketTimeoutException" -> error.postValue(ErrorResponse(504, message.toString()))
                        "java.net.UnknownHostException"  ->  error.postValue(ErrorResponse(-1, message.toString()))
                        else  ->  error.postValue(ErrorResponse(502, message.toString()))
                    }
                }
            })
    }

    internal fun postApiCreateUser(vmAuth: AuthViewModel) {
        Log.d("postApiCreateUser", "postApiCreateUser body - ${UserRegPost(email, login, userName, pass)}")
        vmAuth.postCreateUser(UserRegPost(email, login, userName, pass),
            object : CoroutinesErrorHandler {
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

    internal fun FragmentActivity.registrationFinish(
        vmUser: UserDataViewModel,
        apiResponse: UserRegResponse
    ) {
        vmUser.apply{
            createUserDataAfterReg(apiResponse)
            changeVMModeGuest(false)
        }.let {
            onStartupEdit()
        }
    }

    private fun FragmentActivity.onStartupEdit() {
        getPreferences(Context.MODE_PRIVATE)
            .edit()
            .putBoolean(HelloPageViewModel.PK_FIRST_LAUNCH, true)
            .apply()
    }

    override fun onCleared() {
        if (::job.isInitialized) job.cancel()
        super.onCleared()
    }
}