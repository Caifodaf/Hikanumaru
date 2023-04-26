package ru.android.hikanumaruapp.api.api.main

import androidx.lifecycle.MutableLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import ru.android.hikanumaruapp.api.init.ApiResponse
import ru.android.hikanumaruapp.api.init.BaseViewModel
import ru.android.hikanumaruapp.api.init.CoroutinesErrorHandler
import ru.android.hikanumaruapp.api.models.UserRegResponse
import ru.android.hikanumaruapp.model.GenresMainModel
import ru.android.hikanumaruapp.model.Manga
import ru.android.hikanumaruapp.model.MangaList
import ru.android.hikanumaruapp.model.MangaPageTextDate
import javax.inject.Inject

@HiltViewModel
class MainApiViewModel @Inject constructor(private val mainRepository: MainRepository) : BaseViewModel() {

    val userInfoResponse: MutableLiveData<ApiResponse<UserRegResponse>> by lazy { MutableLiveData<ApiResponse<UserRegResponse>>() }
    val mangaPageResponse: MutableLiveData<ApiResponse<Manga>> by lazy { MutableLiveData<ApiResponse<Manga>>() }

    fun getUserInfo(coroutinesErrorHandler: CoroutinesErrorHandler) =
        baseRequest(userInfoResponse, coroutinesErrorHandler) {
            mainRepository.getUserMe()
        }

    fun getMangaPage(post: String,coroutinesErrorHandler: CoroutinesErrorHandler) =
        baseRequest(mangaPageResponse, coroutinesErrorHandler) {
            mainRepository.getMangaPage(post)
        }



    fun getMainPageGenres(mainGenresResponse: MutableLiveData<ApiResponse<List<GenresMainModel>>>,
        coroutinesErrorHandler: CoroutinesErrorHandler) =
        baseRequest(mainGenresResponse, coroutinesErrorHandler) {
            mainRepository.getGenres()
        }
    fun getMainPageManga(
        mainMangaResponse: MutableLiveData<ApiResponse<List<MangaList>>>,
        coroutinesErrorHandler: CoroutinesErrorHandler) =
        baseRequest(mainMangaResponse, coroutinesErrorHandler) {
            mainRepository.getMainPageManga()
        }
    fun getMainPageManhva(
        mainManhvaResponse: MutableLiveData<ApiResponse<List<MangaList>>>,
        coroutinesErrorHandler: CoroutinesErrorHandler) =
        baseRequest(mainManhvaResponse, coroutinesErrorHandler) {
            mainRepository.getMainPageManhva()
        }

    fun getMainPagePopular(
        mainPopularResponse: MutableLiveData<ApiResponse<List<MangaList>>>,
        coroutinesErrorHandler: CoroutinesErrorHandler) =
        baseRequest(mainPopularResponse, coroutinesErrorHandler) {
            mainRepository.getMainPagePopular()
        }
}