package ru.android.hikanumaruapp.local.storage

import android.content.Context
import androidx.datastore.preferences.core.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import ru.android.hikanumaruapp.api.models.UserRegResponse
import ru.android.hikanumaruapp.local.storage.UserDataStore.UserScheme.FIELD_CREATE_TIME
import ru.android.hikanumaruapp.local.storage.UserDataStore.UserScheme.FIELD_EMAIL
import ru.android.hikanumaruapp.local.storage.UserDataStore.UserScheme.FIELD_GUEST
import ru.android.hikanumaruapp.local.storage.UserDataStore.UserScheme.FIELD_ID
import ru.android.hikanumaruapp.local.storage.UserDataStore.UserScheme.FIELD_LOGIN
import ru.android.hikanumaruapp.local.storage.UserDataStore.UserScheme.FIELD_NAME
import ru.android.hikanumaruapp.local.storage.UserDataStore.UserScheme.FIELD_ROLES
import ru.android.hikanumaruapp.local.storage.UserDataStore.UserScheme.FIELD_UPDATE_TIME
import ru.android.hikanumaruapp.model.UserInfo

class UserDataStore (private val context: Context){

    val USER_KEY = stringPreferencesKey("user_data")

    object UserScheme {
        val FIELD_ID = stringPreferencesKey("0")
        val FIELD_EMAIL = stringPreferencesKey("exmaple@mail.com")
        val FIELD_LOGIN = stringPreferencesKey("login")
        val FIELD_NAME = stringPreferencesKey("name")
        val FIELD_ROLES = stringSetPreferencesKey(listOf<String>("Role").toString())
        val FIELD_CREATE_TIME = stringPreferencesKey("00/00/00")
        val FIELD_UPDATE_TIME = stringPreferencesKey("00/00/00")
        val FIELD_GUEST = booleanPreferencesKey(false.toString())
    }

    suspend fun getUserData(): Flow<UserInfo?> {
        val id = context.dataStore.data.map { preferences -> preferences[FIELD_ID] }.toString()
        val email = context.dataStore.data.map { preferences -> preferences[FIELD_EMAIL] }.toString()
        val login = context.dataStore.data.map { preferences -> preferences[FIELD_LOGIN] }.toString()
        val name = context.dataStore.data.map { preferences -> preferences[FIELD_NAME] }.toString()
        val role = context.dataStore.data.map { preferences -> preferences[FIELD_ROLES] }.toList()
        val create = context.dataStore.data.map { preferences -> preferences[FIELD_CREATE_TIME] }.toString()
        val update = context.dataStore.data.map { preferences -> preferences[FIELD_UPDATE_TIME] }.toString()
        val modeGuestOut = when(context.dataStore.data.map { preferences -> preferences[FIELD_GUEST] }.toString()){
            "true" -> true
            "false" -> false
            else -> false
        }

        return flow {
            emit(UserInfo(id, email, login, name, role, create, update, modeGuestOut))
        }
    }

    suspend fun saveUserDataAfterReg(newUser: UserRegResponse) {
        context.dataStore.edit { pref -> pref[FIELD_ID] =  newUser.id }
        context.dataStore.edit { pref -> pref[FIELD_EMAIL] =  newUser.email }
        context.dataStore.edit { pref -> pref[FIELD_LOGIN] =  newUser.login }
        context.dataStore.edit { pref -> pref[FIELD_NAME] =  newUser.username }
        context.dataStore.edit { pref -> pref[FIELD_ROLES] = newUser.roles.toSet() }
        context.dataStore.edit { pref -> pref[FIELD_CREATE_TIME] =  newUser.createdAt }
        context.dataStore.edit { pref -> pref[FIELD_UPDATE_TIME] =  newUser.updateAt }
        context.dataStore.edit { pref -> pref[FIELD_GUEST] =  newUser.isGuest }
    }

    suspend fun saveUpdateUserData(newUser: UserInfo) {
        context.dataStore.edit { preferences ->
            preferences[USER_KEY] = ""
        }
    }

    suspend fun deleteUserData() {
        context.dataStore.edit { preferences ->
            preferences.remove(USER_KEY)
        }
    }

}