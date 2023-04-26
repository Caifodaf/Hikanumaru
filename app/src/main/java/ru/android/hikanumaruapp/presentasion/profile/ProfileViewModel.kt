package ru.android.hikanumaruapp.presentasion.profile

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import ru.android.hikanumaruapp.local.user.UserSharedPreferenceAdapter
import ru.android.hikanumaruapp.local.storage.library.LibraryBase
import ru.android.hikanumaruapp.local.user.UserDataViewModel
import ru.android.hikanumaruapp.model.Manga
import ru.android.hikanumaruapp.presentasion.profile.folders.FolderListDefaultObject
import ru.android.hikanumaruapp.presentasion.profile.folders.FoldersLibraryAdapter
import ru.android.hikanumaruapp.presentasion.profile.folders.LibraryAdapter
import ru.android.hikanumaruapp.utilits.room.ConvertersRoom
import ru.android.hikanumaruapp.utilits.room.GsonParser
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(): ViewModel(), UserSharedPreferenceAdapter,FolderListDefaultObject {

    private lateinit var jobUser: Job
    private lateinit var jobLibrary: Job
    internal var libraryDB : LibraryBase ? = null

    lateinit var libraryAdapter: LibraryAdapter
    lateinit var folderAdapter: FoldersLibraryAdapter

    //val user: MutableLiveData<UserInfo> by lazy { MutableLiveData<UserInfo>() }
    val library: MutableLiveData<List<Manga>> by lazy { MutableLiveData<List<Manga>>() }

    internal fun Context.getUser(vmUser: UserDataViewModel) {
        vmUser.apply{
            getUserData()
        }.let {}
    }

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