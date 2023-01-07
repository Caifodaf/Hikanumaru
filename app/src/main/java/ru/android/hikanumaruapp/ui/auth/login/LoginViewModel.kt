package ru.android.hikanumaruapp.ui.auth.login

import android.util.Log
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import ru.android.hikanumaruapp.R
import ru.android.hikanumaruapp.api.Repository
import ru.android.hikanumaruapp.api.models.ErrorResponse
import ru.android.hikanumaruapp.api.models.TokenJWT
import ru.android.hikanumaruapp.api.models.UserAuthPost
import ru.android.hikanumaruapp.api.models.UserRegResponse
import ru.android.hikanumaruapp.local.storage.SharedPreferenceAdapter
import ru.android.hikanumaruapp.utilits.navigation.Events
import ru.android.hikanumaruapp.utilits.navigation.NavigationFragmentinViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val repository: Repository) : ViewModel(), SharedPreferenceAdapter {

    private lateinit var job: Job
    private val emitter = Events.Emitter()

    private val _tokenResponse = MutableLiveData<TokenJWT>()
    private val tokenResponse: LiveData<TokenJWT>
        get() = _tokenResponse

    private val _user = MutableLiveData<UserRegResponse>()
    val user: LiveData<UserRegResponse>
        get() = _user

    private val _error = MutableLiveData<ErrorResponse>()
    val error: MutableLiveData<ErrorResponse>
        get() = _error

    internal fun postApiAuth(login: String, pass: String, context: FragmentActivity) {
        job = viewModelScope.launch(Dispatchers.IO) {
            repository.postAuthUser(UserAuthPost(login, pass))
                .catch { exception ->
                    Log.e("ErrorApi", exception.message.toString())
                    _error.postValue(ErrorResponse(0, "postApiAuth catch - $exception", 0))
                }
                .collect {
                    Log.d("ApiResult", "postApiAuth ${it.code()}")
                    Log.d("ApiResult", "postApiAuth ${it.message()}")
                    Log.d("ApiResult", "postApiAuth ${it.body()}")

                    when (it.code()) {
                        200 -> {
                            _tokenResponse.postValue(it.body())
                            apiAuthGetUser(context)
                        }
                        401 -> _error.postValue(ErrorResponse(401, it.body()!!.message.toString(), it.code()))
                        else -> _error.postValue(ErrorResponse(1, it.body()!!.message.toString(), it.code()))
                    }
                }
        }
    }

    private fun apiAuthGetUser(context: FragmentActivity) {
        job = viewModelScope.launch(Dispatchers.IO) {
            repository.getUserMe(tokenResponse.value!!.token)
                .catch { exception ->
                    Log.e("ErrorApi", exception.message.toString())
                    _error.postValue(ErrorResponse(0, "apiAuthGetUser catch - $exception", 0))
                }
                .collect {
                    Log.d("ApiResult", "apiAuthGetUser ${it.code()}")
                    Log.d("ApiResult", "apiAuthGetUser ${it.message()}")
                    Log.d("ApiResult", "apiAuthGetUser ${it.body()}")

                    when (it.code()) {
                        200 -> {
                            saveUserReg(it, context)
                            saveToken(tokenResponse.value!!.token, tokenResponse.value!!.refresh, context)
                            changeModeGuest(false,context)
                            changeAuth(context,true)
                            _user.postValue(it.body())

                            emitter.emitAndExecute(
                                NavigationFragmentinViewModel.NavigationFrag(
                                R.id.navigation_home, null))
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