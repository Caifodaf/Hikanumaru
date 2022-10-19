package ru.android.hikanumaruapp.api.models

data class UserRegPost(
    var email:String = "",
    var login:String = "",
    var username:String = "",
    var plainPassword:String = ""
)

data class UserAuthPost(
    var login:String = "",
    var password:String = "",
)