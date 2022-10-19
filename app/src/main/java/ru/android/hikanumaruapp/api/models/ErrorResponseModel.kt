package ru.android.hikanumaruapp.api.models

import com.squareup.moshi.Json

data class ErrorResponse(
    var codeApp:Int,
    var message:String? = "",
    var codeApi:Int,
)

