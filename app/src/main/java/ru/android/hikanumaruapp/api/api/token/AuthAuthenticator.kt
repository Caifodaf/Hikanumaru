package ru.android.hikanumaruapp.api.api.token

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import ru.android.hikanumaruapp.api.ApiUrl
import ru.android.hikanumaruapp.api.init.SerializeNullsFactory
import ru.android.hikanumaruapp.api.models.TokenJWT
import ru.android.hikanumaruapp.api.token.TokenManager
import javax.inject.Inject

class AuthAuthenticator @Inject constructor(
    private val tokenManager: TokenManager,
): Authenticator {

    private val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

    private fun provideMoshi(): Moshi {
        return Moshi
            .Builder()
            .add(SerializeNullsFactory())
            .add(KotlinJsonAdapterFactory())
            .build()
    }

    override fun authenticate(route: Route?, response: Response): Request? {
        val refresh = runBlocking {
            tokenManager.getRefresh().first()
        }
        return runBlocking {
            val newToken = getNewToken(refresh)

            if (!newToken.isSuccessful || newToken.body() == null) { //Couldn't refresh the token, so restart the login process
                tokenManager.deleteToken()
            }

            newToken.body()?.let { jwt ->
                tokenManager.saveToken(jwt)
                response.request.newBuilder()
                    .header("Authorization", "Bearer ${jwt.token}")
                    .build()
            }
        }
    }

    private suspend fun getNewToken(refreshToken: String?): retrofit2.Response<TokenJWT> {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        val okHttpClient = OkHttpClient.Builder().addInterceptor(loggingInterceptor).build()

        val retrofit = Retrofit.Builder()
            .baseUrl(ApiUrl.BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(provideMoshi()))
            .client(okHttpClient)
            .build()
        val service = retrofit.create(AuthApiService::class.java)
        //return service.postUpdateJWT("Bearer $refreshToken")
        return service.postUpdateJWT(refreshToken?:"")
    }
}