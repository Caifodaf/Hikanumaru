package ru.android.hikanumaruapp.api.api.token

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path
import ru.android.hikanumaruapp.api.models.CheckAvailabilityResponse
import ru.android.hikanumaruapp.api.models.TokenJWT
import ru.android.hikanumaruapp.api.models.UserAuthPost
import ru.android.hikanumaruapp.api.models.UserRegPost

interface AuthApiService {

    // Check Email
    @POST("user/check-email-availability/{email}")
    @Headers("Content-Type: application/json")
    suspend fun getCheckMail(@Path("email") post:String): Response<CheckAvailabilityResponse>

    // Check Login
    @POST("user/check-login-availability/{login}")
    @Headers("Content-Type: application/json")
    suspend fun getCheckLogin(@Path("login") post:String): Response<CheckAvailabilityResponse>

    // Get Token | Login
    @POST("token")
    @Headers("Content-Type: application/json")
    suspend fun postLogin(@Body post: UserAuthPost): Response<TokenJWT>

    // Create User
    @POST("user/create")
    @Headers("Content-Type: application/json")
    suspend fun postCreateUser(@Body post: UserRegPost): Response<TokenJWT>

    // Update refresh token
    @POST("token/refresh")
    @Headers("Content-Type: application/json")
    suspend fun postUpdateJWT(@Body post: String): Response<TokenJWT>
}