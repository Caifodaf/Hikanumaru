package ru.android.hikanumaruapp.api.api.token

import androidx.lifecycle.MutableLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import ru.android.hikanumaruapp.api.init.ApiResponse
import ru.android.hikanumaruapp.api.init.BaseViewModel
import ru.android.hikanumaruapp.api.init.CoroutinesErrorHandler
import ru.android.hikanumaruapp.api.models.CheckAvailabilityResponse
import ru.android.hikanumaruapp.api.models.TokenJWT
import ru.android.hikanumaruapp.api.models.UserAuthPost
import ru.android.hikanumaruapp.api.models.UserRegPost
import ru.android.hikanumaruapp.data.model.GenresMainModel
import ru.android.hikanumaruapp.data.model.MangaList
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(private val authRepository: AuthRepository) : BaseViewModel() {

    val checkEmailResponse: MutableLiveData<ApiResponse<CheckAvailabilityResponse>> by lazy { MutableLiveData<ApiResponse<CheckAvailabilityResponse>>() }
    val checkLoginResponse: MutableLiveData<ApiResponse<CheckAvailabilityResponse>> by lazy { MutableLiveData<ApiResponse<CheckAvailabilityResponse>>() }

    val loginResponse: MutableLiveData<ApiResponse<TokenJWT>> by lazy { MutableLiveData<ApiResponse<TokenJWT>>() }
    val createUserResponse: MutableLiveData<ApiResponse<TokenJWT>> by lazy { MutableLiveData<ApiResponse<TokenJWT>>() }

    fun getCheckMail(post: String, coroutinesErrorHandler: CoroutinesErrorHandler) =
        baseRequest(checkEmailResponse, coroutinesErrorHandler) {
            authRepository.getCheckMail(post)
        }

    fun getCheckLogin(post: String, coroutinesErrorHandler: CoroutinesErrorHandler) =
        baseRequest(checkLoginResponse, coroutinesErrorHandler) {
            authRepository.getCheckLogin(post)
        }

    fun postLogin(post: UserAuthPost, coroutinesErrorHandler: CoroutinesErrorHandler) =
        baseRequest(loginResponse, coroutinesErrorHandler) {
            authRepository.postLogin(post)
        }

    fun postCreateUser(post: UserRegPost, coroutinesErrorHandler: CoroutinesErrorHandler) =
        baseRequest(createUserResponse, coroutinesErrorHandler) {
            authRepository.postCreateUser(post)
        }



    fun getMainPageGenres(
        mainGenresResponse: MutableLiveData<ApiResponse<List<GenresMainModel>>>,
        coroutinesErrorHandler: CoroutinesErrorHandler) =
        baseRequest(mainGenresResponse, coroutinesErrorHandler) {
            authRepository.getGenres()
        }
    fun getMainPageManga(
        mainMangaResponse: MutableLiveData<ApiResponse<List<MangaList>>>,
        coroutinesErrorHandler: CoroutinesErrorHandler) =
        baseRequest(mainMangaResponse, coroutinesErrorHandler) {
            authRepository.getMainPageManga()
        }
    fun getMainPageManhva(
        mainManhvaResponse: MutableLiveData<ApiResponse<List<MangaList>>>,
        coroutinesErrorHandler: CoroutinesErrorHandler) =
        baseRequest(mainManhvaResponse, coroutinesErrorHandler) {
            authRepository.getMainPageManhva()
        }

    fun getMainPagePopular(
        mainPopularResponse: MutableLiveData<ApiResponse<List<MangaList>>>,
        coroutinesErrorHandler: CoroutinesErrorHandler) =
        baseRequest(mainPopularResponse, coroutinesErrorHandler) {
            authRepository.getMainPagePopular()
        }
}
