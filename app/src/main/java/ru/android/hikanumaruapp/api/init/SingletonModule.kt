package ru.android.hikanumaruapp.api.init

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import ru.android.hikanumaruapp.api.ApiUrl
import ru.android.hikanumaruapp.api.api.main.MainApiService
import ru.android.hikanumaruapp.api.api.token.AuthApiService
import ru.android.hikanumaruapp.api.api.token.AuthAuthenticator
import ru.android.hikanumaruapp.api.api.token.AuthInterceptor
import ru.android.hikanumaruapp.api.token.TokenManager
import javax.inject.Singleton

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "jwt")

@Module
@InstallIn(SingletonComponent::class)
class SingletonModule {

    private fun provideMoshi(): Moshi {
        return Moshi
            .Builder()
            .add(SerializeNullsFactory())
            .add(KotlinJsonAdapterFactory())
            .build()
    }

    @Singleton
    @Provides
    fun provideTokenManager(@ApplicationContext context: Context): TokenManager = TokenManager(context)

    @Singleton
    @Provides
    fun provideOkHttpClient(
        authInterceptor: AuthInterceptor,
        authAuthenticator: AuthAuthenticator,
    ): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        return OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .addInterceptor(loggingInterceptor)
            .authenticator(authAuthenticator)
            .build()
    }

    @Singleton
    @Provides
    fun provideAuthInterceptor(tokenManager: TokenManager): AuthInterceptor =
        AuthInterceptor(tokenManager)

    @Singleton
    @Provides
    fun provideAuthAuthenticator(tokenManager: TokenManager): AuthAuthenticator =
        AuthAuthenticator(tokenManager)

    @Singleton
    @Provides
    fun provideRetrofitBuilder(): Retrofit.Builder =
        Retrofit.Builder()
            .baseUrl(ApiUrl.BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(provideMoshi()))

    @Singleton
    @Provides
    fun provideAuthTokenAPIService(retrofit: Retrofit.Builder): AuthApiService =
        retrofit
            .build()
            .create(AuthApiService::class.java)

    @Singleton
    @Provides
    fun provideMainAPIService(okHttpClient: OkHttpClient,retrofit: Retrofit.Builder): MainApiService =
        retrofit
            .client(okHttpClient)
            .build()
            .create(MainApiService::class.java)

}