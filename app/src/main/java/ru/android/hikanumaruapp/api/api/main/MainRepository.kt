package ru.android.hikanumaruapp.api.api.main

import ru.android.hikanumaruapp.api.init.apiRequestFlow
import ru.android.hikanumaruapp.api.models.UserRegPost
import javax.inject.Inject


class MainRepository @Inject constructor(
    private val mainApiService: MainApiService,
) {

    // Create User after registration
    fun postCreateUser(post: UserRegPost) = apiRequestFlow {
        mainApiService.postCreateUser(post)
    }

    // Get User with token
    fun getUserMe() = apiRequestFlow {
        mainApiService.getUserMe()
    }

    // getMangaPage
    fun getMangaPage(post: String) = apiRequestFlow {
        mainApiService.getMangaPage(post)
    }


    // Get Genres | Home
    fun getGenres() = apiRequestFlow {
        mainApiService.getGenres()
    }

    // Get Manga | Home
    fun getMainPageManga() = apiRequestFlow {
        mainApiService.getMainPageManga()
    }

    // Get Manhva | Home
    fun getMainPageManhva() = apiRequestFlow {
        mainApiService.getMainPageManhva()
    }

    // Get Popular | Home
    fun getMainPagePopular() = apiRequestFlow {
        mainApiService.getMainPagePopular()
    }
}