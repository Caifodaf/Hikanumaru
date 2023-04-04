package ru.android.hikanumaruapp.ui.auth.registration.state.two

import android.util.Log
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import ru.android.hikanumaruapp.R
import ru.android.hikanumaruapp.api.Repository
import ru.android.hikanumaruapp.api.models.*
import ru.android.hikanumaruapp.local.storage.JWTShared
import ru.android.hikanumaruapp.local.storage.SharedPreferenceAdapter
import ru.android.hikanumaruapp.utilits.navigation.Events
import ru.android.hikanumaruapp.utilits.navigation.NavigationFragmentinViewModel
import javax.inject.Inject


@HiltViewModel
class RegistrationStateTwoViewModel @Inject constructor() : ViewModel(), SharedPreferenceAdapter,
    JWTShared {

    private val repository: Repository = Repository()

    private lateinit var job: Job
    private val emitter = Events.Emitter()

    private var email: String = ""
    private var pass: String = ""
    private var login: String = ""
    private var userName: String = ""

    private var isCheckLogin: Boolean = false
    private var checkLogin: String = ""

    private var isCreateUser: Boolean = false

    val loginResponse: MutableLiveData<CodeMessageResponse> by lazy { MutableLiveData<CodeMessageResponse>() }
    val userResponse: MutableLiveData<UserRegResponse> by lazy { MutableLiveData<UserRegResponse>() }
    val loginError: MutableLiveData<ErrorResponse> by lazy { MutableLiveData<ErrorResponse>() }

    internal fun setDataStageOne(email: String, pass: String) {
        this.email = email
        this.pass = pass
    }

    //TODO NOt Founded Login Check Now
    internal fun setDataStageTwo(login: String, userName: String) {
        this.login = login
        this.userName = userName
    }

    //TODO NOt Founded Login Check Now
    fun apiCheckLogin(login: String, userName: String) {
        if (checkLogin == login && login.isNotBlank() && isCheckLogin) {
            setDataStageTwo(checkLogin, userName)
            // todo fast
        } else {
            setDataStageTwo(login, userName)

            val loginLoad = login

            job = viewModelScope.launch(Dispatchers.IO) {
                repository.getCheckLogin(loginLoad)
                    .catch { exception ->
                        Log.e("ErrorApi", exception.message.toString())
                        loginError.postValue(ErrorResponse(0, "apiCheckLogin catch - $exception", 0))
                    }
                    .collect {
                        Log.d("ApiResult", "apiCheckLogin ${it.code()}")
                        Log.d("ApiResult", "apiCheckLogin ${it.message()}")
                        Log.d("ApiResult", "apiCheckLogin ${it.body()}")

                        when (it.code()) {
                            200 -> {
                                if (it.body()!!.success) {
                                    isCheckLogin = true
                                    checkLogin = loginLoad
                                } else
                                    isCheckLogin = false

                                loginResponse.postValue(CodeMessageResponse(it.isSuccessful,
                                    it.message()))
                            }
                            401 -> loginError.postValue(ErrorResponse(401,
                                it.body()!!.message.toString(),
                                it.code()))
                            else -> loginError.postValue(ErrorResponse(1,
                                it.body()!!.message.toString(),
                                it.code()))
                        }
                    }
            }
        }
    }

    fun postCreateUser(context: FragmentActivity) {
        val userData = UserRegPost("caif@mail1.com11h","Caifodaf2","Caifodaf1","11111")
        //val userData = UserRegPost(email, login, userName, pass)

        job = viewModelScope.launch(Dispatchers.IO) {
            repository.postCreateUser(userData)
                .catch { exception ->
                    Log.e("ErrorApi", exception.message.toString())
                    loginError.postValue(ErrorResponse(0, "postCreateUser catch - $exception", 0))
                }
                .collect { response ->
                    Log.d("ApiResult", "postCreateUser ${response.code()}")
                    Log.d("ApiResult", "postCreateUser ${response.message()}")
                    Log.d("ApiResult", "postCreateUser ${response.body()}")

                    when (response.code()) {
                        200 -> {
                            context.saveToken(
                                response.body()!!.token.toString(),
                                response.body()!!.refresh.toString()
                            )
                            isCreateUser = true
                            context.getCreatedUser()
                        }
                        400 -> {
                            if (!isCreateUser) {
                                loginError.postValue(ErrorResponse(400, response.message(), response.code()))
                            }
                        }
                        401 -> loginError.postValue(ErrorResponse(401,
                            response.body()!!.message.toString(),
                            response.code()))
                        else -> loginError.postValue(ErrorResponse(1,
                            response.body()!!.message.toString(),
                            response.code()))
                    }
                }
        }
    }

    private fun FragmentActivity.getCreatedUser() {
        job = viewModelScope.launch(Dispatchers.IO) {
            repository.getUserMe()
                .catch { exception ->
                    Log.e("ErrorApi", exception.message.toString())
                    loginError.postValue(ErrorResponse(0, "getCreatedUser catch - $exception", 0))
                }
                .collect { response ->
                    Log.d("ApiResult", "getCreatedUser ${response.code()}")
                    Log.d("ApiResult", "getCreatedUser ${response.message()}")
                    Log.d("ApiResult", "getCreatedUser ${response.body()}")

                    when (response.code()) {
                        200 -> {
                            saveUserReg(response)
                            changeModeGuest(false)
                            changeAuth(true)
                            isCreateUser = true
                            emitter.emitAndExecute(
                                NavigationFragmentinViewModel.NavigationFrag(
                                    R.id.navigation_home, null))
                            //finish() ZH
                        }
                        400 -> {
                            if (!isCreateUser) {
                                loginError.postValue(ErrorResponse(400, response.message(), response.code()))
                            }
                        }
                        401 -> loginError.postValue(ErrorResponse(401,
                            response.body()!!.toString(),
                            response.code()))
                        else -> loginError.postValue(ErrorResponse(1,
                            response.body()!!.toString(),
                            response.code()))
                    }
                }
        }
    }

    override fun onCleared() {
        if (::job.isInitialized) job.cancel()
        super.onCleared()
    }
}