package ru.android.hikanumaruapp.data.local.storage.local.home

import android.content.Context
import android.util.Base64
import android.util.Log
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.android.hikanumaruapp.data.PermissionAdapter
import java.io.File
import java.io.FileOutputStream
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

interface CacheSharedPreferenceAdapter {

    //fun Context.saveHomeCache (cache: HomeCacheModel){
    //    getSharedPreferences(CACHE_PREFERENCES,
    //        Context.MODE_PRIVATE).edit().apply {
    //        clear()
    //        putString(CACHE_HOME_DATA_PREFERENCES, Gson().toJson(cache))
    //        apply()
    //    }.let {
    //        saveFunctionCallTime()
    //    }
    //}

    //fun Context.getHomeCache(): Flow<HomeCacheModel?> {
    //    val serializedUser = getSharedPreferences(CACHE_PREFERENCES, Context.MODE_PRIVATE)
    //        .getString(CACHE_HOME_DATA_PREFERENCES, null)
    //    return flow {
    //        emit(
    //            if (serializedUser != null) Gson().fromJson(serializedUser, HomeCacheModel::class.java) else null
    //        )
    //    }
    //}

    companion object {
        //const val CACHE_HOME_DATA_PREFERENCES = "home_data_cache"

        const val CACHE_PREFERENCES = "cache_preferences"
        const val CACHE_HOME_DATA_UPDATE_TIME_PREFERENCES = "home_data_cache"

        const val CACHE_HOME_DATA_FILENAME = "fEFJFCKAWDjodAo2D03d3.txt"
        const val CACHE_HOME_DATA_DIRECTORY = "cache/"
        const val CACHE_HOME_KEY = "FJDHFkj-FJKE-fKO#356Rfj"
    }

    fun Context.checkUpdateTimeHomeCache(action: (Boolean?) -> Unit){
        val callTime = getSharedPreferences(CACHE_PREFERENCES,
            Context.MODE_PRIVATE).getLong(CACHE_HOME_DATA_UPDATE_TIME_PREFERENCES, 0L)
        if (callTime == 0L) {
            action.invoke(true) // если время не сохранено, то возвращаем true
        }else{
            val currentTime = System.currentTimeMillis()
            val elapsedTime = currentTime - callTime
            val hours12 = 12 * 60 * 60 * 1000
            action.invoke(elapsedTime > hours12)
        }
    }

    private fun Context.saveFunctionCallTime() =
        getSharedPreferences(CACHE_PREFERENCES, Context.MODE_PRIVATE)
            .edit()
            .putLong(CACHE_HOME_DATA_UPDATE_TIME_PREFERENCES, System.currentTimeMillis())
            .apply()

    fun Context.saveHomeCache (cache: HomeCacheModel):Boolean{
        //val permissionHelper = PermissionAdapter(this)
        //permissionHelper.requestWriteExternalStoragePermission { isGranted ->
        //    if (isGranted) {
        //        // Разрешение получено, можно использовать WRITE_EXTERNAL_STORAGE
        //    } else {
        //        // Разрешение не получено
        //    }
        //}

        val file = File(CACHE_HOME_DATA_DIRECTORY,CACHE_HOME_DATA_FILENAME)
        try {
            val cacheStr = Gson().toJson(cache)
            val strDone = encryptString(cacheStr,CACHE_HOME_KEY)

            if (strDone == "")
                return false

            file.writeText(strDone)
            saveFunctionCallTime()
            return true
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e("CacheSharedPreferenceAdapter", "saveHomeCache - ${e.localizedMessage}")
            return false
        }
    }

    fun Context.getHomeCache(): Flow<HomeCacheModel?> {
        val file = File(CACHE_HOME_DATA_DIRECTORY,CACHE_HOME_DATA_FILENAME)
        try {
            val content = file.readText()
            val strDone = decryptString(content,CACHE_HOME_KEY)

            if (strDone == "")
                return flow { emit(null)}

            return flow { emit(Gson().fromJson(strDone, HomeCacheModel::class.java))}
        }catch (e: Exception) {
            e.printStackTrace()
            Log.e("CacheSharedPreferenceAdapter", "getHomeCache - ${e.localizedMessage}")
            return flow { emit(null)}
        }
    }

    private fun encryptString(content: String, key: String): String {
        try {
            val secretKey = SecretKeySpec(key.toByteArray(Charsets.UTF_8), "AES")
            val iv = IvParameterSpec(key.substring(0, 16).toByteArray(Charsets.UTF_8))

            // создание шифратора
            val cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING")
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, iv)

            // шифрование данных
            val encrypted = cipher.doFinal(content.toByteArray(Charsets.UTF_8))

            // кодирование в Base64 и возврат зашифрованной строки
            return Base64.encodeToString(encrypted, Base64.DEFAULT)
        } catch (e: Exception) {
            e.printStackTrace()
            return ""
        }
    }

    private fun decryptString(content: String, key: String): String {
        try {
            val secretKey = SecretKeySpec(key.toByteArray(Charsets.UTF_8), "AES")
            val iv = IvParameterSpec(key.substring(0, 16).toByteArray(Charsets.UTF_8))

            // создание дешифратора
            val cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING")
            cipher.init(Cipher.DECRYPT_MODE, secretKey, iv)

            // расшифрование данных
            val decoded = Base64.decode(content, Base64.DEFAULT)
            val decrypted = cipher.doFinal(decoded)

            // возврат расшифрованной строки
            return String(decrypted, Charsets.UTF_8)
        } catch (e: Exception) {
            e.printStackTrace()
            return ""
        }
    }

    fun Context.clearHomeCache() {
        getSharedPreferences(CACHE_PREFERENCES, Context.MODE_PRIVATE).edit()
            .putString(CACHE_HOME_DATA_UPDATE_TIME_PREFERENCES, null)
            .apply()
        val file = File(CACHE_HOME_DATA_FILENAME)
        file.delete()
    }

}