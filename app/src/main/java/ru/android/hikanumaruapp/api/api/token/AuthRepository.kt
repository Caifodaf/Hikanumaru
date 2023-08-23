package ru.android.hikanumaruapp.api.api.token

import ru.android.hikanumaruapp.api.init.apiRequestFlow
import ru.android.hikanumaruapp.api.models.UserAuthPost
import ru.android.hikanumaruapp.api.models.UserRegPost
import javax.inject.Inject


class AuthRepository @Inject constructor(
    private val authApiService: AuthApiService,
) {

    // Check Email
    fun getCheckMail(post: String) = apiRequestFlow {
        authApiService.getCheckMail(post)
    }

    // Check Email
    fun getCheckLogin(post: String) = apiRequestFlow {
        authApiService.getCheckLogin(post)
    }

    // Get Token | Login
    fun postLogin(post: UserAuthPost) = apiRequestFlow {
        authApiService.postLogin(post)
    }

    // Create User
    fun postCreateUser(post: UserRegPost) = apiRequestFlow {
        authApiService.postCreateUser(post)
    }

    // Update refresh token
    fun postUpdateJWT(post: String) = apiRequestFlow {
        authApiService.postUpdateJWT(post)
    }


    // Get Genres | Home
    fun getGenres() = apiRequestFlow {
        authApiService.getGenres()
    }

    // Get Manga | Home
    fun getMainPageManga() = apiRequestFlow {
        authApiService.getMainPageManga()
    }

    // Get Manhva | Home
    fun getMainPageManhva() = apiRequestFlow {
        authApiService.getMainPageManhva()
    }

    // Get Popular | Home
    fun getMainPagePopular() = apiRequestFlow {
        authApiService.getMainPagePopular()
    }


}