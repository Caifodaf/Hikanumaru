package ru.android.hikanumaruapp.presentasion.reader.data

import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withTimeoutOrNull
import retrofit2.Response
import ru.android.hikanumaruapp.api.models.ErrorResponse

fun<T> networkRequestFlow(call: suspend () -> T?): Flow<NetworkResponse<T>> = flow {
    emit(NetworkResponse.Loading)

    withTimeoutOrNull(10 * 1000) {
        val response = call()

        try {
            if (response != null)
                emit(NetworkResponse.Success(data = response))
            else
                emit(NetworkResponse.Failure(404,"parsedError nullPointerException"))
        } catch (e: Exception) {
            emit(NetworkResponse.Failure(400,e.message ?: e.toString() ))
        }
    } ?: emit(NetworkResponse.Failure(408, "Timeout! Please try again."))
}.flowOn(Dispatchers.IO)