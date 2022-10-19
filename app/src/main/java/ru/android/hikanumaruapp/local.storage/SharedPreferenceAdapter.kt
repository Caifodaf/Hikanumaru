package ru.android.hikanumaruapp.local.storage

import android.content.Context
import androidx.fragment.app.FragmentActivity
import com.google.gson.Gson
import retrofit2.Response
import ru.android.hikanumaruapp.api.models.UserRegResponse
import ru.android.hikanumaruapp.model.UserInfo

interface SharedPreferenceAdapter {

    fun deleteUser(requireActivity: FragmentActivity){
        val shUser = requireActivity.getSharedPreferences("user", Context.MODE_PRIVATE)
        val shToken = requireActivity.getSharedPreferences("token", Context.MODE_PRIVATE)
        val shStart = requireActivity.getSharedPreferences("start", Context.MODE_PRIVATE)
        shStart.edit().putBoolean("pref_login_auth", false).apply()

        shUser.edit().clear().apply()
        shToken.edit().clear().apply()
    }

    fun changeAuth (requireActivity: FragmentActivity,state: Boolean){
        val spStart = requireActivity.getSharedPreferences("start",Context.MODE_PRIVATE)
        when(state){
            true->spStart.edit().putBoolean("pref_login_auth", true).apply()
            false->spStart.edit().putBoolean("pref_login_auth", false).apply()
        }
    }

   fun saveUserReg(response: Response<UserRegResponse>, requireActivity: FragmentActivity) {

       val user = UserInfo()
        var shUser = requireActivity.getSharedPreferences("user", Context.MODE_PRIVATE)
        shUser.edit().clear().apply()

        //spEditUser.putString("id",response.body()!!.id)
        //spEditUser.putString("type",response.body()!!.type)
        //spEditUser.putString("mail",response.body()!!.email)
        //spEditUser.putString("login",response.body()!!.login)
        //spEditUser.putString("username",response.body()!!.username)
        //spEditUser.putString("imageCover",response.body()!!.imageCover)
        //spEditUser.putString("imageCoverUpdatedAt",response.body()!!.imageCoverUpdatedAt)
        //spEditUser.putString("imageAvatar",response.body()!!.imageAvatar)
        //spEditUser.putString("imageAvatarUpdatedAt",response.body()!!.imageAvatarUpdatedAt)
        //spEditUser.putString("statusCheckMail",response.body()!!.statusCheckMail)
        //spEditUser.putString("roles", response.body()!!.roles.toString())
        //spEditUser.putString("createdAt",response.body()!!.createdAt)

       user.id = response.body()!!.id
       user.type = response.body()!!.type.toString()
       user.mail = response.body()!!.email
       user.login = response.body()!!.login
       user.userName = response.body()!!.username
       user.imageCover = response.body()?.imageCover.toString()
       user.imageCoverUpdatedAt = response.body()?.imageCoverUpdatedAt.toString()
       user.imageAvatar = response.body()?.imageAvatar.toString()
       user.imageAvatarUpdatedAt = response.body()?.imageAvatarUpdatedAt.toString()
       user.statusCheckMail = response.body()!!.statusCheckMail
       user.sex = "nan"
       user.dateBirth = "nan"
       user.roles = response.body()!!.roles.toString()
       user.createdAt = response.body()!!.createdAt
       user.modeGuest = false

       val serializedUser = Gson().toJson(user)
       shUser.edit().putString("user_info", serializedUser).apply()

       shUser=null
    }

    fun getUserInfo (requireActivity: FragmentActivity): UserInfo? {
        val shUser = requireActivity.getSharedPreferences("user", Context.MODE_PRIVATE)

        val serializedUser = shUser.getString("user_info", null)
        return Gson().fromJson(serializedUser, UserInfo::class.java)
    }

    fun updateSaveUserInfo (user: UserInfo,requireActivity: FragmentActivity){
        val shUser = requireActivity.getSharedPreferences("user", Context.MODE_PRIVATE)

        val serializedUser = Gson().toJson(user)
        shUser.edit().putString("user_info", serializedUser).apply()
    }



    fun changeModeGuest(state:Boolean,requireActivity: FragmentActivity){
        var shUser = requireActivity.getSharedPreferences("user", Context.MODE_PRIVATE)
        when(state) {
            true ->
                shUser.edit().putBoolean("guest_mode", true).apply()
            false ->
                shUser.edit().putBoolean("guest_mode", false).apply()
        }
    }

    fun saveToken(token:String, refresh:String, requireActivity: FragmentActivity) {
        var shToken = requireActivity.getSharedPreferences("token", Context.MODE_PRIVATE)
        shToken.edit().clear().apply()

        var spEditToken = shToken.edit()
        spEditToken.putString("token",token)
        spEditToken.putString("refresh",refresh)
        spEditToken.apply()
        spEditToken=null
        shToken=null
    }

    fun deleteJWT(requireActivity: FragmentActivity){
        var shToken = requireActivity.getSharedPreferences("token", Context.MODE_PRIVATE)
        shToken.edit().clear().apply()

        shToken=null
    }

    fun getRefresh(requireActivity: FragmentActivity):String{
        val shToken = requireActivity.getSharedPreferences("token", Context.MODE_PRIVATE)
        return shToken.getString("refresh",null).toString()
    }

    fun getToken(requireActivity: FragmentActivity):String{
        val shToken = requireActivity.getSharedPreferences("token", Context.MODE_PRIVATE)
        return shToken.getString("token",null).toString()
    }
}