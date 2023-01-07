package ru.android.hikanumaruapp.model

import com.squareup.moshi.Json

data class UserInfo(
    @Json(name = "id") var id: String = "", // todo
    @Json(name = "type") var type: String = "", // todo
    @Json(name = "mail") var mail: String = "",
    @Json(name = "login") var login: String = "",
    @Json(name = "userName") var userName: String = "",
    @Json(name = "imageCover") var imageCover: String = "",
    @Json(name = "imageCoverUpdatedAt") var imageCoverUpdatedAt: String = "",
    @Json(name = "imageAvatar") var imageAvatar: String = "",
    @Json(name = "imageAvatarUpdatedAt") var imageAvatarUpdatedAt: String = "",
    @Json(name = "statusCheckMail") var statusCheckMail: String = "", // todo
    @Json(name = "sex") var sex: String = "",
    @Json(name = "dateBirth") var dateBirth: String = "",
    @Json(name = "roles") var roles: String = "",
    @Json(name = "createdAt") var createdAt: String = "",
    @Json(name = "modeGuest") var modeGuest: Boolean = false // todo
)
