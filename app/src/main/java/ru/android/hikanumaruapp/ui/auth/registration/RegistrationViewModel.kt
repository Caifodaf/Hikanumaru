package ru.android.hikanumaruapp.ui.auth.registration

import android.util.Log
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LiveData
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
import ru.android.hikanumaruapp.local.storage.SharedPreferenceAdapter
import ru.android.hikanumaruapp.utilits.Events
import ru.android.hikanumaruapp.utilits.NavigationFragmentinViewModel
import ru.android.hikanumaruapp.utilits.UIUtils
import javax.inject.Inject


@HiltViewModel
class RegistrationViewModel @Inject constructor(private val repository: Repository) : ViewModel(),
    UIUtils, SharedPreferenceAdapter {

    private lateinit var job: Job
    private val emitter = Events.Emitter()

    private var email:String = ""
    private var pass:String = ""
    private var login:String = ""
    private var userName:String = ""

    private var isCheckLogin: Boolean = false
    private var checkLogin: String = ""

    private var isCheckMail: Boolean = false
    private var checkMail: String = ""

    private var isCreateUser: Boolean = false

    val _loginResponse = MutableLiveData<CodeMessageResponse>()
    val loginResponse: LiveData<CodeMessageResponse>
        get() = _loginResponse

    val _emailResponse = MutableLiveData<CodeMessageResponse>()
    val emailResponse: LiveData<CodeMessageResponse>
        get() = _emailResponse

    val _userResponse = MutableLiveData<UserRegResponse>()
    val userResponse: LiveData<UserRegResponse>
        get() = _userResponse

    private val _error = MutableLiveData<ErrorResponse>()
    val error: MutableLiveData<ErrorResponse>
        get() = _error

    private fun setDataStageOne(email:String, pass:String){
        this.email = email
        this.pass = pass
    }

    private fun setDataStageTwo(login:String,userName:String){
        this.login = login
        this.userName = userName
    }






        fun postCheckEmail(email: String, pass: String) {
        if (checkMail == email && email.isNotBlank() && isCheckMail) {
            setDataStageOne(checkMail, pass)

            // todo fast
        }else{
            setDataStageOne(email, pass)

            val emailLoad = email

            job = viewModelScope.launch(Dispatchers.IO) {
                repository.getCheckEmail(emailLoad)
                    .catch { exception ->
                        Log.e("ErrorApi", exception.message.toString())
                        _error.postValue(ErrorResponse(0, "postCheckEmail catch - $exception", 0))
                    }
                    .collect {
                        Log.d("ApiResult", "postCheckEmail ${it.code()}")
                        Log.d("ApiResult", "postCheckEmail ${it.message()}")
                        Log.d("ApiResult", "postCheckEmail ${it.body()}")

                        when (it.code()) {
                            200 -> {
                                if (it.body()!!.success) {
                                    isCheckMail = true
                                    checkMail = emailLoad
                                }else
                                    isCheckMail = false

                                _emailResponse.postValue(it.body())
                            }
                            401 -> _error.postValue(ErrorResponse(401, it.body()!!.message.toString(), it.code()))
                            else -> _error.postValue(ErrorResponse(1, it.body()!!.message.toString(), it.code()))
                        }
                    }
            }
        }
    }

    fun apiCheckLogin(login:String,userName:String){
        if (checkLogin == login && login.isNotBlank() && isCheckLogin) {
            setDataStageTwo(checkLogin, userName)
            // todo fast
        }else{
            setDataStageTwo(login, userName)

            val loginLoad = login

            job = viewModelScope.launch(Dispatchers.IO) {
                repository.getCheckLogin(loginLoad)
                    .catch { exception ->
                        Log.e("ErrorApi", exception.message.toString())
                        _error.postValue(ErrorResponse(0, "apiCheckLogin catch - $exception", 0))
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
                                }else
                                    isCheckLogin = false

                                _loginResponse.postValue(CodeMessageResponse(it.isSuccessful,it.message(),
                                    it.body()!!.description))
                            }
                            401 -> _error.postValue(ErrorResponse(401, it.body()!!.message.toString(), it.code()))
                            else -> _error.postValue(ErrorResponse(1, it.body()!!.message.toString(), it.code()))
                        }
                    }
            }
        }
    }

    fun postCreateUser(context: FragmentActivity) {
        //val userData = UserRegPost("caif@mail.com11h","Caifodaf1","Caifodaf1","11111")
        val userData = UserRegPost(email, login, userName, pass)

        job = viewModelScope.launch(Dispatchers.IO) {
            repository.postCreateUser(userData)
                .catch { exception ->
                    Log.e("ErrorApi", exception.message.toString())
                    _error.postValue(ErrorResponse(0, "postCreateUser catch - $exception", 0))
                }
                .collect {
                    Log.d("ApiResult", "postCreateUser ${it.code()}")
                    Log.d("ApiResult", "postCreateUser ${it.message()}")
                    Log.d("ApiResult", "postCreateUser ${it.body()}")

                    when (it.code()) {
                        200 -> {
                            saveUserReg(it,context)
                            changeModeGuest(false,context)
                            saveToken(it.body()!!.token.toString(),it.body()!!.refresh.toString(),context)
                            changeAuth(context,true)

                            isCreateUser=true

                            emitter.emitAndExecute(
                                NavigationFragmentinViewModel.NavigationFrag(
                                    R.id.navigation_home, null))
                            context.finish()
                        }
                        400 -> {
                            if(!isCreateUser){
                                error.postValue(ErrorResponse(400,it.message(),it.code()))
                            }
                        }
                        401 -> _error.postValue(ErrorResponse(401, it.body()!!.message.toString(), it.code()))
                        else -> _error.postValue(ErrorResponse(1, it.body()!!.message.toString(), it.code()))
                    }
                }
        }
    }

    override fun onCleared() {
        if(::job.isInitialized) job.cancel()
        super.onCleared()
    }
}