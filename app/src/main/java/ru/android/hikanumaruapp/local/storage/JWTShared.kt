package ru.android.hikanumaruapp.local.storage

import android.content.Context
import androidx.fragment.app.FragmentActivity
import com.google.gson.Gson
import retrofit2.Response
import ru.android.hikanumaruapp.api.models.UserRegResponse
import ru.android.hikanumaruapp.model.UserInfo

interface JWTShared {

    companion object{
        const val TOKEN_APP = "token"
        const val TOKEN_APP_REFRESH = "refresh"
        const val TOKEN_APP_TOKEN = "token"
    }

    fun FragmentActivity.saveToken(token:String, refresh:String) {
        var shToken = getSharedPreferences(TOKEN_APP, Context.MODE_PRIVATE)
        shToken.edit().clear().apply()

        var spEditToken = shToken.edit()
        spEditToken.putString(TOKEN_APP_TOKEN,token)
        spEditToken.putString(TOKEN_APP_REFRESH,refresh)
        spEditToken.apply()
        spEditToken=null
        shToken=null
    }

    fun FragmentActivity.deleteJWT(){
        val shToken = getSharedPreferences(TOKEN_APP, Context.MODE_PRIVATE)
        shToken.edit().clear().apply()
    }

    fun FragmentActivity.getRefresh():String{
        val shToken = getSharedPreferences(TOKEN_APP, Context.MODE_PRIVATE)
        return shToken.getString(TOKEN_APP_REFRESH,null).toString()
    }

    fun FragmentActivity.getToken():String{
        val shToken = getSharedPreferences(TOKEN_APP, Context.MODE_PRIVATE)
        return shToken.getString(TOKEN_APP_TOKEN,null).toString()
    }


}