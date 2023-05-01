package ru.android.hikanumaruapp.data.local.storage.local.home

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class LocalCacheHomeViewModel@Inject constructor() : ViewModel(),CacheSharedPreferenceAdapter {

    val homeListModelCache: MutableLiveData<HomeCacheModel> by lazy { MutableLiveData<HomeCacheModel>() }
//    fun getHomeCacheVM(action: (MutableLiveData<List<HomeCache>>?) -> Unit)

    fun Context.clearHomeCacheVM(){
        viewModelScope.launch(Dispatchers.IO) {
            applicationContext.clearHomeCache()
        }
    }

    fun Context.saveHomeCacheVM(cacheList: HomeCacheModel){
        try {
            viewModelScope.launch(Dispatchers.IO) {
                applicationContext.saveHomeCache(cacheList)
            }
        }catch (e: Exception){
            Log.e("saveHomeCacheVM", "Error Save Cache")
        }
    }

    fun Context.getHomeCacheVM() {
        viewModelScope.launch(Dispatchers.IO) {
            withContext(Dispatchers.IO) {
                applicationContext.getHomeCache().collect {
                    homeListModelCache.postValue(it)
                }
            }
        }
    }

    fun Context.checkUpdateTimeHomeCacheVM():Boolean{
        var isUpdateAllow = false
        viewModelScope.launch(Dispatchers.IO) {
            applicationContext.checkUpdateTimeHomeCache{ isAllow ->
                isUpdateAllow = isAllow ?: false
            } }.let {
            Log.e("saveHomeCacheVM", "isUpdateAllow - $isUpdateAllow")
            return isUpdateAllow
        }
    }

//    suspend fun Context.checkUpdateTimeHomeCacheVM(): Boolean = withContext(Dispatchers.IO) {
//        return@withContext (applicationContext.checkUpdateTimeHomeCache() ?: false)
//    }
}