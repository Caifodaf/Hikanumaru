package ru.android.hikanumaruapp.api.api.token

import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import ru.android.hikanumaruapp.api.token.TokenManager
import javax.inject.Inject

class AuthInterceptor @Inject constructor(
    private val tokenManager: TokenManager,
): Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val token = runBlocking {
            tokenManager.getToken().first()
        }
        val request = chain.request().newBuilder()
        request.addHeader("Authorization", "Bearer $token")
        return chain.proceed(request.build())
    }
}