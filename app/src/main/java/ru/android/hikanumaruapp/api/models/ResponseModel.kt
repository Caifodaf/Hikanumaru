package ru.android.hikanumaruapp.api.models

import com.squareup.moshi.Json

data class UserRegResponse(
    @Json(name = "@id")var id:String = "",
    @Json(name = "@type")var type: String?= "",
    @Json(name = "email")var email:String = "",
    @Json(name = "login")var login:String = "",
    @Json(name = "imageCover")var imageCover:String = "",
    @Json(name = "imageCoverUpdatedAt")var imageCoverUpdatedAt:String = "",
    @Json(name = "imageAvatar")var imageAvatar:String = "",
    @Json(name = "imageAvatarUpdatedAt")var imageAvatarUpdatedAt:String = "",
    @Json(name = "status")var statusCheckMail:String = "",
    @Json(name = "roles")var roles: List<String> = listOf<String>(),
    @Json(name = "createdAt")var createdAt:String = "",
    @Json(name = "username")var username:String = "",

    var token:String? = "",
    var refresh:String? = "",
    var success:Boolean? = false,
    var message:String? = "",
)

data class CodeMessageResponse(
    @Json(name = "success")var success:Boolean = false,
    @Json(name = "message")var message:String = "",
    @Json(name = "hydra:description")var description:String? = "",
)

data class TokenJWT(
    @Json(name = "token")var token:String = "",
    @Json(name = "refresh_token")var refresh:String = "",
    @Json(name = "success")var success:Boolean? = false,
    @Json(name = "message")var message:String? = "",
)
