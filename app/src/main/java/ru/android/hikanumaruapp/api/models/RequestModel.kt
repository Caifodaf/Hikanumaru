package ru.android.hikanumaruapp.api.models

import com.squareup.moshi.Json

data class UserRegPost(
    @Json(name = "email")var email:String = "",
    @Json(name = "login")var login:String = "",
    @Json(name = "username")var username:String = "",
    @Json(name = "password")var password:String = "",
)

data class UserAuthPost(
     @Json(name = "login")var login:String = "",
     @Json(name = "password")var password:String = "",
)

data class CreativeWorksRequestModel(
    var page:Int = 1,
    var search:String? = null,
    var type:List<String>? = null,
    var publicationStatus:List<String>? = null,
    var translationStatus:List<String>? = null,
    var source:String? = null,
    var itemsPerPage:Int? = 10,
    var genres:List<String>? = null,
    var releaseYear:List<Int>? = null,
    var releaseYearStart:Int? = null,
    var releaseYearEnd:Int? = null,
    var orderSortTitle:String? = null,
    var orderSortYear:String? = null,
)