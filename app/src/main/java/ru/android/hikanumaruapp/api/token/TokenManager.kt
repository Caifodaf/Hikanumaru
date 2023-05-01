package ru.android.hikanumaruapp.api.token

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.android.hikanumaruapp.api.init.dataStore
import ru.android.hikanumaruapp.api.models.TokenJWT

class TokenManager(private val context: Context) {
    companion object {
        private val TOKEN_KEY = stringPreferencesKey("jwt_token")
        private val REFRESH_KEY = stringPreferencesKey("jwt_refresh")
    }

    fun getToken(): Flow<String?> {
        return context.dataStore.data.map { preferences ->
            preferences[TOKEN_KEY]
        }
    }

    fun getRefresh(): Flow<String?> {
        return context.dataStore.data.map { preferences ->
            preferences[REFRESH_KEY]
        }
    }

    suspend fun saveToken(token: TokenJWT) {
        context.dataStore.edit { preferences ->
            preferences[TOKEN_KEY] = token.token
            preferences[REFRESH_KEY] = token.refresh
        }
    }

    suspend fun deleteToken() {
        context.dataStore.edit { preferences ->
            preferences.remove(TOKEN_KEY)
            preferences.remove(REFRESH_KEY)
        }
    }
}