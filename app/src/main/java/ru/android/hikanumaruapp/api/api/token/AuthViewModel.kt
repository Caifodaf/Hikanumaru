package ru.android.hikanumaruapp.api.api.token

import androidx.lifecycle.MutableLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import ru.android.hikanumaruapp.api.init.ApiResponse
import ru.android.hikanumaruapp.api.init.BaseViewModel
import ru.android.hikanumaruapp.api.init.CoroutinesErrorHandler
import ru.android.hikanumaruapp.api.models.TokenJWT
import ru.android.hikanumaruapp.api.models.UserAuthPost
import ru.android.hikanumaruapp.api.models.UserRegPost
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(private val authRepository: AuthRepository) : BaseViewModel() {

    val loginResponse: MutableLiveData<ApiResponse<TokenJWT>> by lazy { MutableLiveData<ApiResponse<TokenJWT>>() }
    val createUserResponse: MutableLiveData<ApiResponse<TokenJWT>> by lazy { MutableLiveData<ApiResponse<TokenJWT>>() }

    fun postLogin(post: UserAuthPost, coroutinesErrorHandler: CoroutinesErrorHandler) =
        baseRequest(loginResponse, coroutinesErrorHandler) {
            authRepository.postLogin(post)
        }

    fun postCreateUser(post: UserRegPost, coroutinesErrorHandler: CoroutinesErrorHandler) =
        baseRequest(createUserResponse, coroutinesErrorHandler) {
            authRepository.postCreateUser(post)
        }
}