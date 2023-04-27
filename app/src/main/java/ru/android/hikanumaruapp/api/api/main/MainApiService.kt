package ru.android.hikanumaruapp.api.api.main

import retrofit2.Response
import retrofit2.http.*
import ru.android.hikanumaruapp.api.models.TokenJWT
import ru.android.hikanumaruapp.api.models.UserRegPost
import ru.android.hikanumaruapp.api.models.UserRegResponse
import ru.android.hikanumaruapp.data.model.GenresMainModel
import ru.android.hikanumaruapp.data.model.Manga
import ru.android.hikanumaruapp.data.model.MangaList

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




    // Get CreativeWorks Collection
    @GET("creativeWorks")
    @Headers("Content-Type: application/json")
    suspend fun getCreativeWorksCollection(
        @Query("page") page:Int = 1,
        @Query("search") search:String? = null,
        @Query("type[]") type:List<String>? = null,
        @Query("publicationStatus[]") publicationStatus:List<String>? = null,
        @Query("translationStatus[]") translationStatus:List<String>? = null,
        @Query("source") source:String? = null,
        @Query("itemsPerPage") itemsPerPage:Int? = 10,
        @Query("genres.id[]") genres:List<String>? = null,
        @Query("releaseYear[]") releaseYear:List<Int>? = null,
        @Query("releaseYear[gte]") releaseYearStart:Int? = null,
        @Query("releaseYear[lte]") releaseYearEnd:Int? = null,
        @Query("order[title]") orderSortTitle:String? = null,
        @Query("order[releaseYear]") orderSortYear:String? = null,
    ): Response<List<MangaList>>


    // Get Main Page Genres
    @GET("genres")
    @Headers("Content-Type: application/json")
    //suspend fun getGenres(@Body body: BodyGenresApiModel): Response<GenresMainModel>
    suspend fun getGenres(): Response<List<GenresMainModel>>
}