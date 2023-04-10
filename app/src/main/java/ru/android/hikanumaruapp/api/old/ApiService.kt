package ru.android.hikanumaruapp.api.old

import retrofit2.Response
import retrofit2.http.*
import ru.android.hikanumaruapp.api.models.*

interface ApiService {

    //@POST("launches/query")
    //@Headers("Content-Type: application/json")
    //suspend fun getLaunchesList(@Body body:BodyLaunchersModel): Response<LaunchesModelResponse>
//
    //@GET("crew")
    //@Headers("Content-Type: application/json")
    //suspend fun getCrewList(): Response<List<CrewsModel>>


// Update

    @POST("token/refresh")
    @Headers("Content-Type: application/json")
    suspend fun postUpdateJWT(@Body post: String): Response<TokenJWT>

    // Auntification User

    //@GET("/api/token")
    //@Headers("Content-Type: application/ld+json")
    //suspend fun getUserMe(@Header("Authorization Bearer ") token: String): Response<UserRegResponse>

    //@GET("/api/token")
    //@Headers("Content-Type: application/ld+json")
    //suspend fun getUserMe(): Response<UserRegResponse>

    @POST("login_check")
    @Headers("Content-Type: application/json")
    suspend fun postAuthUser(@Body post: UserAuthPost): Response<TokenJWT>

    // Registration

    @GET("users/email/{email}/check")
    @Headers("Content-Type: application/json")
    //suspend fun getCheckEmail(post: String): Response<CodeMessageResponse>
    suspend fun getCheckEmail(@Path("email") post:String): Response<CodeMessageResponse>

    @GET("/api/users/login/{login}/check")
    @Headers("Content-Type: application/json")
    //suspend fun getCheckEmail(post: String): Response<CodeMessageResponse>
    suspend fun getCheckLogin(@Path("login") post:String): Response<CodeMessageResponse>

    //@POST("/api/user/create")
    //@Headers("Content-Type: application/json")
    //suspend fun postCreateUser(@Body post: UserRegPost): Response<TokenJWT>

    //@GET("genres")
    //@Headers("Content-Type: application/json")
    //fun getListGenresTest(): Call<MutableList<GenresModel>>

    //@GET(".api/users/check/login{login}")
    //@Headers("Content-Type: application/json")
    //fun getListTest1(@Path("login") login:String): Single<ResponseModel>

}