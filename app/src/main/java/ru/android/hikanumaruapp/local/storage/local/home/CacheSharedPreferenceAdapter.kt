package ru.android.hikanumaruapp.local.storage.local.home

import android.content.Context
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.android.hikanumaruapp.local.user.UserSharedPreferenceAdapter
import ru.android.hikanumaruapp.model.UserInfo

interface CacheSharedPreferenceAdapter {

    companion object {
        const val CACHE_PREFERENCES = "cache_preferences"
        const val CACHE_HOME_DATA_PREFERENCES = "home_data_cache"

        const val CACHE_HOME_DATA_UPDATE_TIME_PREFERENCES = "home_data_cache"
    }

    private fun Context.saveFunctionCallTime() {
        val currentTime = System.currentTimeMillis()
        val sharedPreferences = getSharedPreferences(CACHE_PREFERENCES,
            Context.MODE_PRIVATE).edit().apply {
                putLong(CACHE_HOME_DATA_UPDATE_TIME_PREFERENCES, currentTime)
                apply()
        }
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

    fun Context.saveHomeCache (cache: HomeCacheModel){
        getSharedPreferences(CACHE_PREFERENCES,
            Context.MODE_PRIVATE).edit().apply {
            clear()
            putString(CACHE_HOME_DATA_PREFERENCES, Gson().toJson(cache))
            apply()
        }.let {
            saveFunctionCallTime()
        }
    }

    fun Context.getHomeCache(): Flow<HomeCacheModel?> {
        val serializedUser = getSharedPreferences(CACHE_PREFERENCES, Context.MODE_PRIVATE)
            .getString(CACHE_HOME_DATA_PREFERENCES, null)

        val result = if (serializedUser != null)
            Gson().fromJson(serializedUser, HomeCacheModel::class.java)
        else
            null

        return flow<HomeCacheModel> {
            result?.let { emit(it) }
        }
    }

    fun Context.clearHomeCache (){
        getSharedPreferences(CACHE_PREFERENCES,
            Context.MODE_PRIVATE).edit().apply {
            clear()
            putString(CACHE_HOME_DATA_PREFERENCES, null)
            apply()
        }
    }
}