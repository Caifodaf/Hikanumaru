package ru.android.hikanumaruapp.api.api.token

import retrofit2.Response
import retrofit2.http.*
import ru.android.hikanumaruapp.api.models.CheckAvailabilityResponse
import ru.android.hikanumaruapp.api.models.TokenJWT
import ru.android.hikanumaruapp.api.models.UserAuthPost
import ru.android.hikanumaruapp.api.models.UserRegPost
import ru.android.hikanumaruapp.model.BodyGenresApiModel
import ru.android.hikanumaruapp.model.GenresMainModel
import ru.android.hikanumaruapp.model.Manga
import ru.android.hikanumaruapp.model.MangaList

interface AuthApiService {
    // Check Email
    @GET("user/check-email-availability/{email}")
    @Headers("Content-Type: application/json")
    suspend fun getCheckMail(@Path("email") post:String): Response<CheckAvailabilityResponse>

    // Check Login
    @GET("user/check-login-availability/{login}")
    @Headers("Content-Type: application/json")
    suspend fun getCheckLogin(@Path("login") post:String): Response<CheckAvailabilityResponse>

    // Get Token | Login
    @POST("token")
    @Headers("Content-Type: application/json")
    suspend fun postLogin(@Body post: UserAuthPost): Response<TokenJWT>

    // Create User /api/user/register
    @POST("user/register")
    @Headers("Content-Type: application/json")
    suspend fun postCreateUser(@Body post: UserRegPost): Response<TokenJWT>

    // Update refresh token
    @POST("token/refresh")
    @Headers("Content-Type: application/json")
    suspend fun postUpdateJWT(@Body post: String): Response<TokenJWT>



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