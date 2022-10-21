package ru.android.hikanumaruapp.api

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.flow
import retrofit2.Response
import ru.android.hikanumaruapp.api.init.RetrofitClient
import ru.android.hikanumaruapp.api.models.*
import javax.inject.Inject

class Repository @Inject constructor(){

    //suspend fun getLaunchesList(body: BodyLaunchersModel): Flow<LaunchesModelResponse?> {
    //    return flow {
    //        emit(RetrofitClient.api.getLaunchesList(body).body()!!)
    //    }
    //}

    suspend fun postAuthUser(post: UserAuthPost): Flow<Response<TokenJWT>> {
        return flow {
            emit(RetrofitClient.api.postAuthUser(post))
        }
    }

    suspend fun getUserMe(token: String): Flow<Response<UserRegResponse>> {
        return flow {
            emit(RetrofitClient.api.getUserMe(token))
        }
    }

    suspend fun getCheckEmail(post: String): Flow<Response<CodeMessageResponse>> {
        return flow {
            emit(RetrofitClient.api.getCheckEmail(post))
        }
    }

    suspend fun getCheckLogin(post: String): Flow<Response<CodeMessageResponse>> {
        return flow {
            emit(RetrofitClient.api.getCheckEmail(post))
        }
    }

    suspend fun postCreateUser(post: UserRegPost): Flow<Response<UserRegResponse>> {
        return flow {
            emit(RetrofitClient.api.postCreateUser(post))
        }
    }


}