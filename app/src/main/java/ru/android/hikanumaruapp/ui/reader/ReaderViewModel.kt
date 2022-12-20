package ru.android.hikanumaruapp.ui.reader

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.catch
import ru.android.hikanumaruapp.model.Chapter
import ru.android.hikanumaruapp.provider.Provider
import ru.android.hikanumaruapp.provider.ReaderProvider
import ru.android.hikanumaruapp.ui.reader.model.ReaderChapter
import java.io.IOException
import java.io.Reader
import javax.inject.Inject

@HiltViewModel
class ReaderViewModel @Inject constructor(private val provider:ReaderProvider) : ViewModel() {

    private lateinit var job: Job

    private val listLoadPages = mutableListOf<ReaderChapter>()
    val loadPages: MutableLiveData<List<ReaderChapter>> by lazy {
        MutableLiveData<List<ReaderChapter>>()
    }





    internal fun getDataChapter(url: String) {
        job = viewModelScope.launch(Dispatchers.IO) {
            provider.preloadChapterFirst(url,listLoadPages)
                .catch { exception ->
                    Log.e("ErrorApi", exception.message.toString())
                    // Todo error add
                    //loadNewChapter=true
                    //loadChapterError=true
                }
                .collect {
                    //loadNewChapter=false
                    loadPages.postValue(it.requireNoNulls())
                }
        }
    }


}