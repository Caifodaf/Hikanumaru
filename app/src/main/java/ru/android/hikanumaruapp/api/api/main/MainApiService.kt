package ru.android.hikanumaruapp.api.api.main

import retrofit2.Response
import retrofit2.http.*
import ru.android.hikanumaruapp.api.models.TokenJWT
import ru.android.hikanumaruapp.api.models.UserRegPost
import ru.android.hikanumaruapp.api.models.UserRegResponse
import ru.android.hikanumaruapp.model.GenresMainModel
import ru.android.hikanumaruapp.model.Manga
import ru.android.hikanumaruapp.model.MangaList

interface MainApiService {
    // Create User after registration
    @POST("user/register")
    @Headers("Content-Type: application/json")
    suspend fun postCreateUser(@Body post: UserRegPost): Response<TokenJWT>

    // Get User with token
    @GET("users/me")
    @Headers("Content-Type: application/json")
    suspend fun getUserMe(): Response<UserRegResponse>

    // Get Manga Page
    @GET("creativeWork/{id}")
    @Headers("Content-Type: application/json")
    suspend fun getMangaPage(@Path("id") post:String): Response<Manga>




    // Get Main Page Genres
    @GET("genres")
    @Headers("Content-Type: application/json")
    //suspend fun getGenres(@Body body: BodyGenresApiModel): Response<GenresMainModel>
    suspend fun getGenres(): Response<List<GenresMainModel>>

    // Get Main Page Manga
    @GET("creativeWorks")
    @Headers("Content-Type: application/json")
    suspend fun getMainPageManga(): Response<List<MangaList>>

    // Get Main Page Manhva
    @GET("creativeWorks")
    @Headers("Content-Type: application/json")
    suspend fun getMainPageManhva(): Response<List<MangaList>>

    // Get Main Page Popular
    @GET("creativeWorks")
    @Headers("Content-Type: application/json")
    suspend fun getMainPagePopular(): Response<List<MangaList>>
}