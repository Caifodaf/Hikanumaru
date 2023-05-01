package ru.android.hikanumaruapp.data.local.user

import android.content.Context
import android.content.Context.MODE_PRIVATE
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.android.hikanumaruapp.api.models.UserRegResponse
import ru.android.hikanumaruapp.data.local.storage.local.home.HomeCacheModel
import ru.android.hikanumaruapp.data.model.UserInfo

interface UserSharedPreferenceAdapter {

    companion object {
        const val USER_PREFERENCES_NAME = "user_preferences"
        const val USER_DATA_PREFERENCES = "user_data"

        const val USER_PREFERENCES_MODE = "user_mode"

        const val USER_FIELD_ID = "user_id"
        const val USER_FIELD_EMAIL = "user_email"
        const val USER_FIELD_LOGIN = "user_login"
        const val USER_FIELD_NAME = "user_name"
        const val USER_FIELD_ROLES = "user_roles"
        const val USER_FIELD_CREATE_TIME = "user_create"
        const val USER_FIELD_UPDATE_TIME = "user_update"
        const val USER_FIELD_GUEST = "user_is_guest"
    }

    fun Context.createUserReg(userRegResponse: UserRegResponse) {
        val user = UserInfo().apply {
            id = userRegResponse.id
            email = userRegResponse.email
            login = userRegResponse.login
            username = userRegResponse.username
            roles = userRegResponse.roles
            createdAt = userRegResponse.createdAt
            updateAt = userRegResponse.updateAt
            modeGuest = false
        }

        getSharedPreferences(USER_PREFERENCES_NAME, MODE_PRIVATE).edit().apply {
            clear()
            putString(USER_DATA_PREFERENCES, Gson().toJson(user))
            apply()
        }
    }

    fun Context.getUserInfo(): Flow<UserInfo?> {
        val serializedUser = getSharedPreferences(USER_PREFERENCES_NAME, MODE_PRIVATE)
            .getString(USER_DATA_PREFERENCES, null)
        return flow {
            emit(if (serializedUser != null) Gson().fromJson(serializedUser, UserInfo::class.java) else null)
        }
    }

    fun Context.updateSaveUserInfo (user: UserInfo){
        getSharedPreferences(USER_PREFERENCES_NAME, MODE_PRIVATE).edit().apply {
            clear()
            putString(USER_DATA_PREFERENCES, Gson().toJson(user))
            apply()
        }
    }

    fun Context.changeModeGuest(state:Boolean){
        getSharedPreferences(USER_PREFERENCES_NAME, MODE_PRIVATE).apply {
            edit().putBoolean(USER_PREFERENCES_MODE, state).apply()
        }
    }



























//    fun Context.deleteUser(){
//        getSharedPreferences(USER_PREFERENCES_NAME, MODE_PRIVATE).edit().clear().apply()
//
//        val shUser = getSharedPreferences("user", Context.MODE_PRIVATE)
//        val shToken = getSharedPreferences("token", Context.MODE_PRIVATE)
//        val shStart = getSharedPreferences("start", Context.MODE_PRIVATE)
//        shStart.edit().putBoolean("pref_login_auth", false).apply()
//
//        shUser.edit().clear().apply()
//        shToken.edit().clear().apply()
//    }

   //fun FragmentActivity.changeAuth (state: Boolean){
   //    val spStart = getSharedPreferences("start",Context.MODE_PRIVATE)
   //    when(state){
   //        true->spStart.edit().putBoolean("pref_login_auth", true).apply()
   //        false->spStart.edit().putBoolean("pref_login_auth", false).apply()
   //    }
   //}


}