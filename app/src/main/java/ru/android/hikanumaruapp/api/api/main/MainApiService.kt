package ru.android.hikanumaruapp.api.api.main

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import ru.android.hikanumaruapp.api.models.TokenJWT
import ru.android.hikanumaruapp.api.models.UserRegPost
import ru.android.hikanumaruapp.api.models.UserRegResponse

interface MainApiService {
    // Create User after registration
    @POST("user/register")
    @Headers("Content-Type: application/json")
    suspend fun postCreateUser(@Body post: UserRegPost): Response<TokenJWT>

    // Get User with token
    @GET("user/me")
    @Headers("Content-Type: application/json")
    suspend fun getUserMe(): Response<UserRegResponse>
}