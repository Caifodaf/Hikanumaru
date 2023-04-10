package ru.android.hikanumaruapp.api.api.token

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST
import ru.android.hikanumaruapp.api.models.TokenJWT
import ru.android.hikanumaruapp.api.models.UserAuthPost
import ru.android.hikanumaruapp.api.models.UserRegPost

interface AuthApiService {
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