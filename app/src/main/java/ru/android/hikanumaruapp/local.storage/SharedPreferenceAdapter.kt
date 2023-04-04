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

    fun FragmentActivity.changeAuth (state: Boolean){
        val spStart = getSharedPreferences("start",Context.MODE_PRIVATE)
        when(state){
            true->spStart.edit().putBoolean("pref_login_auth", true).apply()
            false->spStart.edit().putBoolean("pref_login_auth", false).apply()
        }
    }

   fun FragmentActivity.saveUserReg(response: Response<UserRegResponse>) {

       val user = UserInfo()
        var shUser = getSharedPreferences("user", Context.MODE_PRIVATE)
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
       //user.type = response.body()!!.type.toString()
       user.mail = response.body()!!.email
       user.login = response.body()!!.login
       user.userName = response.body()!!.username
       //user.imageCover = response.body()?.imageCover.toString()
       //user.imageCoverUpdatedAt = response.body()?.imageCoverUpdatedAt.toString()
       //user.imageAvatar = response.body()?.imageAvatar.toString()
       //user.imageAvatarUpdatedAt = response.body()?.imageAvatarUpdatedAt.toString()
       //user.statusCheckMail = response.body()!!.statusCheckMail
       user.sex = "nan"
       user.dateBirth = "nan"
       user.roles = response.body()!!.roles.toString()
       user.createdAt = response.body()!!.createdAt
       user.modeGuest = false

       val serializedUser = Gson().toJson(user)
       shUser.edit().putString("user_info", serializedUser).apply()

       shUser=null
    }

    private fun createUserReg(requireActivity: FragmentActivity) {

        val user = UserInfo()
        var shUser = requireActivity.getSharedPreferences("user", Context.MODE_PRIVATE)
        shUser.edit().clear().apply()

        user.id = "1233452"
        user.type = "читатель"
        user.mail = "xample@mail.com"
        user.login = "caifodaf"
        user.userName = "Caifodaf__"
        user.imageCover = "null"
        user.imageCoverUpdatedAt = "null"
        user.imageAvatar = "null"
        user.imageAvatarUpdatedAt = "null"
        user.statusCheckMail = "null"
        user.sex = "nan"
        user.dateBirth = "nan"
        user.roles = "читатель"
        user.createdAt = "null"
        user.modeGuest = false

        val serializedUser = Gson().toJson(user)
        shUser.edit().putString("user_info", serializedUser).apply()

        shUser=null
    }

    fun getUserInfo (requireActivity: FragmentActivity): UserInfo? {
        val user = createUserReg(requireActivity)
        val shUser = requireActivity.getSharedPreferences("user", Context.MODE_PRIVATE)

        val serializedUser = shUser.getString("user_info", null)
        return Gson().fromJson(serializedUser, UserInfo::class.java)
    }

    fun updateSaveUserInfo (user: UserInfo,requireActivity: FragmentActivity){
        val shUser = requireActivity.getSharedPreferences("user", Context.MODE_PRIVATE)

        val serializedUser = Gson().toJson(user)
        shUser.edit().putString("user_info", serializedUser).apply()
    }



    fun FragmentActivity.changeModeGuest(state:Boolean){
        var shUser = getSharedPreferences("user", Context.MODE_PRIVATE)
        when(state) {
            true ->
                shUser.edit().putBoolean("guest_mode", true).apply()
            false ->
                shUser.edit().putBoolean("guest_mode", false).apply()
        }
    }


}