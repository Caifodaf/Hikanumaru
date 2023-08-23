package ru.android.hikanumaruapp.presentasion.reader.data

sealed class NetworkResponse<out T> {
    object Loading: NetworkResponse<Nothing>()

    data class Success<out T>(
        val data: T
    ): NetworkResponse<T>()

    data class Failure(
        val code: Int,
        val errorMessage: String,
    ): NetworkResponse<Nothing>()
}