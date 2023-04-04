package ru.android.hikanumaruapp.api.models

import com.squareup.moshi.Json

data class UserRegPost(
    @Json(name = "email")var email:String = "",
    @Json(name = "login")var login:String = "",
    @Json(name = "username")var username:String = "",
    @Json(name = "password")var password:String = ""
)

data class UserAuthPost(
     @Json(name = "login")var login:String = "",
     @Json(name = "password")var password:String = "",
)