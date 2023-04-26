package ru.android.hikanumaruapp.api.init

import android.util.Log
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withTimeoutOrNull
import retrofit2.Response
import ru.android.hikanumaruapp.api.models.ErrorResponse

fun<T> apiRequestFlow(call: suspend () -> Response<T>): Flow<ApiResponse<T>> = flow {
    emit(ApiResponse.Loading)

    withTimeoutOrNull(25000) {
        val response = call()

        try {
            Log.d("apiRequestFlow", "body - ${response.body()}") //todo
            if (response.isSuccessful) {
                response.body()?.let { data ->
                    emit(ApiResponse.Success(data))
                }
            } else {
                response.errorBody()?.let { error ->
                    error.close()
                    val parsedError: ErrorResponse = Gson().fromJson(error.charStream(), ErrorResponse::class.java)
                    emit(ApiResponse.Failure(parsedError.message, parsedError.code))
                }
            }
        } catch (e: Exception) {
            Log.d("apiRequestFlow", "apiRequestFlow catch body - ${response}")
            emit(ApiResponse.Failure(e.message ?: e.toString(), 400))
        }
    } ?: emit(ApiResponse.Failure("Timeout! Please try again.", 408))
}.flowOn(Dispatchers.IO)