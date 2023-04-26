package ru.android.hikanumaruapp.model

import com.squareup.moshi.Json

data class UserInfo(
    @Json(name = "id") var id:String = "",
    @Json(name = "email") var email:String = "",
    @Json(name = "login") var login:String = "",
    @Json(name = "usernames") var username:String = "",
    @Json(name = "roles") var roles: List<String> = listOf(),
    @Json(name = "createTime") var createdAt:String = "",
    @Json(name = "updateTime") var updateAt:String = "",
    var imageAvatar: String? = null,
    var imageCover: String? = null,

//    @Json(name = "type") var type: String = "", // todo
//    @Json(name = "imageCoverUpdatedAt") var imageCoverUpdatedAt: String = "",
//    @Json(name = "imageAvatarUpdatedAt") var imageAvatarUpdatedAt: String = "",
//    @Json(name = "statusCheckMail") var statusCheckMail: String = "", // todo
//    @Json(name = "sex") var sex: String = "",
//    @Json(name = "dateBirth") var dateBirth: String = "",
    @Json(name = "modeGuest") var modeGuest: Boolean = false // todo
)
