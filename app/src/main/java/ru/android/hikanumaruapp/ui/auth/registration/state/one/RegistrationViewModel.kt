package ru.android.hikanumaruapp.ui.auth.registration.state.one

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import ru.android.hikanumaruapp.api.old.Repository
import ru.android.hikanumaruapp.api.models.*
import ru.android.hikanumaruapp.local.storage.SharedPreferenceAdapter
import javax.inject.Inject


@HiltViewModel
class RegistrationViewModel @Inject constructor(private val repository: Repository) : ViewModel(), SharedPreferenceAdapter {

    private lateinit var job: Job

    private var email: String = ""
    private var pass: String = ""

    private var isCheckMail: Boolean = false
    private var checkMail: String = ""

    val emailResponse: MutableLiveData<CodeMessageResponse> by lazy { MutableLiveData<CodeMessageResponse>() }
    val emailError: MutableLiveData<ErrorResponse> by lazy { MutableLiveData<ErrorResponse>() }

    //TODO NOt Founded Email Check Now
    internal fun setDataStageOne(email: String, pass: String) {
        this.email = email
        this.pass = pass
    }

    //TODO NOt Founded Email Check Now
    fun postCheckEmailStateOne(email: String, pass: String) {
        if (checkMail == email && email.isNotBlank() && isCheckMail) {
            setDataStageOne(checkMail, pass)
            // todo fast
        } else {
            setDataStageOne(email, pass)

            val emailLoad = email

            job = viewModelScope.launch(Dispatchers.IO) {
                repository.getCheckEmail(emailLoad)
                    .catch { exception ->
                        Log.e("ErrorApi", exception.message.toString())
                        emailError.postValue(ErrorResponse(0, "postCheckEmail catch - $exception"))
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
                                } else
                                    isCheckMail = false

                                emailResponse.postValue(it.body())
                            }
                            401 -> emailError.postValue(ErrorResponse(401,
                                it.body()!!.message.toString(), ))
                        }
                    }
            }
        }
    }

    override fun onCleared() {
        if (::job.isInitialized) job.cancel()
        super.onCleared()
    }
}