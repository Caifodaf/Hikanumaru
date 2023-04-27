package ru.android.hikanumaruapp.api.api.main

import retrofit2.http.Query
import ru.android.hikanumaruapp.api.init.apiRequestFlow
import ru.android.hikanumaruapp.api.models.CreativeWorksRequestModel
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
    fun getCreativeWorksCollection(filter: CreativeWorksRequestModel?)
    = apiRequestFlow {
        if (filter == null)
            mainApiService.getCreativeWorksCollection(
                page = 1,
                itemsPerPage = 10,
            )
        else
            mainApiService.getCreativeWorksCollection(
                page = filter.page,
                search = filter.search,
                type = filter.type,
                publicationStatus = filter.publicationStatus,
                translationStatus = filter.translationStatus,
                source = filter.source,
                itemsPerPage = filter.itemsPerPage,
                genres = filter.genres,
                releaseYear = filter.releaseYear,
                releaseYearStart = filter.releaseYearStart,
                releaseYearEnd = filter.releaseYearEnd,
                orderSortTitle = filter.orderSortTitle,
                orderSortYear = filter.orderSortYear,
            )
    }


    // Get Genres | Home
    fun getGenres() = apiRequestFlow {
        mainApiService.getGenres()
    }
}