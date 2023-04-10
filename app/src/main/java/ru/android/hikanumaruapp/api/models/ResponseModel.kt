package ru.android.hikanumaruapp.api.models

import com.squareup.moshi.Json

data class UserRegResponse(
    @Json(name = "id")var id:String = "",
    @Json(name = "email")var email:String = "",
    @Json(name = "login")var login:String = "",
    @Json(name = "usernames")var username:String = "",
    @Json(name = "roles")var roles: List<String> = listOf<String>(),
    @Json(name = "createTime")var createdAt:String = "",
    @Json(name = "updateTime")var updateAt:String = "",
    var isGuest:Boolean = false,
)

data class CodeMessageResponse(
    @Json(name = "success")var success:Boolean = false,
    @Json(name = "message")var message:String = "",
)

data class TokenJWT(
    @Json(name = "token")var token:String = "",
    @Json(name = "refreshToken")var refresh:String = "",
//    @Json(name = "success")var success:Boolean? = false,
//    @Json(name = "message")var message:String? = "",
)
