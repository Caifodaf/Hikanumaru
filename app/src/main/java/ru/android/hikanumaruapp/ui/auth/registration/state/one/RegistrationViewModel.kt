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
import ru.android.hikanumaruapp.api.Repository
import ru.android.hikanumaruapp.api.models.*
import ru.android.hikanumaruapp.local.storage.SharedPreferenceAdapter
import javax.inject.Inject


@HiltViewModel
class RegistrationViewModel @Inject constructor(private val repository: Repository) : ViewModel(), SharedPreferenceAdapter {

    companion object{
        const val DEFAULT_BUTTON_VIEW = 0
        const val ACTIVE_BUTTON_VIEW = 1
        const val LOADING_BUTTON_VIEW = 2
        const val ERROR_BUTTON_VIEW = 3

        private const val LENGTH_LOGIN_MIN = 5
        private const val LENGTH_LOGIN_MAX = 40
        val LENGTH_LOGIN_RANGE = LENGTH_LOGIN_MIN..LENGTH_LOGIN_MAX

        private const val LENGTH_NAME_MIN = 3
        private const val LENGTH_NAME_MAX = 40
        val LENGTH_NAME_RANGE = LENGTH_NAME_MIN..LENGTH_NAME_MAX

        private const val LENGTH_PASSWORD_MIN = 5
        private const val LENGTH_PASSWORD_MAX = 40
        val LENGTH_PASSWORD_RANGE = LENGTH_PASSWORD_MIN..LENGTH_PASSWORD_MAX
    }

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
                        emailError.postValue(ErrorResponse(0, "postCheckEmail catch - $exception", 0))
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
                                it.body()!!.message.toString(),
                                it.code()))
                            else -> emailError.postValue(ErrorResponse(1,
                                it.body()!!.message.toString(),
                                it.code()))
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