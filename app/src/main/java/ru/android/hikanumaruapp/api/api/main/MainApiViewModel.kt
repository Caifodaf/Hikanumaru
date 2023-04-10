package ru.android.hikanumaruapp.api.api.main

import androidx.lifecycle.MutableLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import ru.android.hikanumaruapp.api.init.ApiResponse
import ru.android.hikanumaruapp.api.init.BaseViewModel
import ru.android.hikanumaruapp.api.init.CoroutinesErrorHandler
import ru.android.hikanumaruapp.api.models.UserRegResponse
import javax.inject.Inject

@HiltViewModel
class MainApiViewModel @Inject constructor(private val mainRepository: MainRepository) : BaseViewModel() {

    val userInfoResponse: MutableLiveData<ApiResponse<UserRegResponse>> by lazy { MutableLiveData<ApiResponse<UserRegResponse>>() }

    fun getUserInfo(coroutinesErrorHandler: CoroutinesErrorHandler) =
        baseRequest(userInfoResponse, coroutinesErrorHandler) {
            mainRepository.getUserMe()
        }
}