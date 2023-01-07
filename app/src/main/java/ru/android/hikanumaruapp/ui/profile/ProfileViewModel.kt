package ru.android.hikanumaruapp.ui.profile

import android.util.Log
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import ru.android.hikanumaruapp.local.storage.SharedPreferenceAdapter
import ru.android.hikanumaruapp.local.storage.library.LibraryBase
import ru.android.hikanumaruapp.model.Manga
import ru.android.hikanumaruapp.model.UserInfo
import ru.android.hikanumaruapp.ui.profile.folders.FolderListDefaultObject
import ru.android.hikanumaruapp.ui.profile.folders.FoldersLibraryAdapter
import ru.android.hikanumaruapp.utilits.room.ConvertersRoom
import ru.android.hikanumaruapp.utilits.room.GsonParser
import ru.android.hikanumaruapp.utilits.room.JsonParser
import java.lang.Exception
import java.lang.reflect.Type
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(): ViewModel(), SharedPreferenceAdapter,FolderListDefaultObject {

    private lateinit var jobUser: Job
    private lateinit var jobLibrary: Job
    internal var libraryDB : LibraryBase ? = null

    val user: MutableLiveData<UserInfo> by lazy { MutableLiveData<UserInfo>() }
    val library: MutableLiveData<List<Manga>> by lazy { MutableLiveData<List<Manga>>() }

    fun initBDLibrary(context: ProfileFragment) {
        try {
            val roomConverter = ConvertersRoom(GsonParser(Gson()))
            libraryDB = Room.databaseBuilder(
                context.requireContext(),
                LibraryBase::class.java, "manga-library.db"
            ).addTypeConverter(roomConverter)
                .build()
            getLibrary()
        }catch (e: Exception){
            Log.e("ErrorDB", "Library $e")
        }
    }

    fun getUser(context: FragmentActivity) {
        jobUser = viewModelScope.launch(Dispatchers.IO) {
            user.postValue(getUserInfo(context))
        }
    }

    internal fun getLibrary() {
        jobLibrary = viewModelScope.launch(Dispatchers.IO) {
            val list = libraryDB!!.LibraryDao().getAllLibrary()
            //convertOutBD(list)
            library.postValue(list)
        }
    }

    //private fun convertOutBD(list: List<Manga>) {
    //    val collectionType: Type = object : TypeToken<Collection<Manga?>?>() {}.type
    //    val enums: Collection<Manga> = Gson().fromJson(str, collectionType)
    //    page = enums.toMutableList()
    //}
//
    //private fun convertInBD() {
    //    val str = Gson().toJson(listPage)
    //}

    fun setDefaultTagsList(adapter: FoldersLibraryAdapter) {
        adapter.setMain(setListDefaultTags())
    }


    override fun onCleared() {
        if(::jobUser.isInitialized) jobUser.cancel()
        if(::jobLibrary.isInitialized) jobLibrary.cancel()
        super.onCleared()
    }


}