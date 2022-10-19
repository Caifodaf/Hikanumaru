package ru.android.hikanumaruapp.ui.auth.registration

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import ru.android.hikanumaruapp.R
import ru.android.hikanumaruapp.api.Repository
import ru.android.hikanumaruapp.api.models.CodeMessageResponse
import ru.android.hikanumaruapp.api.models.ErrorResponse
import ru.android.hikanumaruapp.api.models.TokenJWT
import ru.android.hikanumaruapp.api.models.UserAuthPost
import ru.android.hikanumaruapp.utilits.Events
import javax.inject.Inject

@HiltViewModel
class RegistrationViewModel @Inject constructor(private val repository: Repository) : ViewModel() {

    private lateinit var job: Job
    private val emitter = Events.Emitter()

    private var email:String = ""
    private var pass:String = ""
    private var login:String = ""
    private var userName:String = ""

    private var isCheckMail: Boolean = false
    private var checkMail: String = ""

    val _emailResponse = MutableLiveData<CodeMessageResponse>()
    val emailResponse: LiveData<CodeMessageResponse>
        get() = _emailResponse

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

















}