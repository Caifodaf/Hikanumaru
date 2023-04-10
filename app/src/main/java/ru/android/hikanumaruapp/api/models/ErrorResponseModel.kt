package ru.android.hikanumaruapp.api.models

import com.squareup.moshi.Json

data class ErrorResponse(
    val code: Int,
    val message: String
)

