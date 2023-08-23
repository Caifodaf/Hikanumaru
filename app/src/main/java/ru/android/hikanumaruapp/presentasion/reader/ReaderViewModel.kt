package ru.android.hikanumaruapp.presentasion.reader

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import ru.android.hikanumaruapp.api.init.CoroutinesErrorHandler
import ru.android.hikanumaruapp.api.models.ErrorResponse
import ru.android.hikanumaruapp.data.model.Manga
import ru.android.hikanumaruapp.data.model.reader.ReaderChapter
import ru.android.hikanumaruapp.presentasion.reader.data.ParserViewModel
import ru.android.hikanumaruapp.presentasion.reader.viewer.pager.ReaderPagerConst.Companion.NEXT
import ru.android.hikanumaruapp.presentasion.reader.viewer.pager.ReaderPagerConst.Companion.PREV
import javax.inject.Inject

@HiltViewModel
class ReaderViewModel @Inject constructor() : ViewModel() {

    private lateinit var job: Job

    lateinit var currentChapterID: String
    lateinit var creativeWorkPage: Manga

    var checkLoadChapterPrev: Boolean = false
    var checkLoadChapterNext: Boolean = false

    val error: MutableLiveData<ErrorResponse> by lazy { MutableLiveData<ErrorResponse>() }
    val loadChapter: MutableLiveData<ReaderChapter> by lazy { MutableLiveData<ReaderChapter>() }
    val preloadChapterNext: MutableLiveData<ReaderChapter> by lazy { MutableLiveData<ReaderChapter>() }
    val preloadChapterPrev: MutableLiveData<ReaderChapter> by lazy { MutableLiveData<ReaderChapter>() }

    fun getDataChapter(vmParser: ParserViewModel, url: String, source: String) {
        vmParser.getChapterReader(source, url,
            object : CoroutinesErrorHandler {
                override fun onError(cause: Throwable?, message: String) {
                    errorHandler(cause, message) }
            })
    }

    fun preloadDataChapterPrev(vmParser: ParserViewModel, url: String, source: String) {
        checkLoadChapterPrev = true
        vmParser.getPreloadChapterReader(PREV, source, url,
            object : CoroutinesErrorHandler {
                override fun onError(cause: Throwable?, message: String) {
                    errorHandler(cause, message) }
            })
    }

    fun preloadDataChapterNext(vmParser: ParserViewModel, url: String, source: String) {
        checkLoadChapterNext = true
        vmParser.getPreloadChapterReader(NEXT, source, url,
            object : CoroutinesErrorHandler {
                override fun onError(cause: Throwable?, message: String) {
                    errorHandler(cause, message) }
            })
    }














    private fun errorHandler(cause: Throwable?, message: String){
        Log.e("ErrorNetwork", "Reader getDataChapter rvm $message")
        when (cause.toString().substringBefore(':')) {
            "java.net.SocketTimeoutException" -> error.postValue(ErrorResponse(504,
                message.toString()))
            "java.net.UnknownHostException" -> error.postValue(ErrorResponse(-1,
                message.toString()))
            else -> error.postValue(ErrorResponse(502, message.toString()))
        }
    }

































    override fun onCleared() {
        super.onCleared()
        if(::job.isInitialized) job.cancel()
        //if (handlerImageSaver != null)
        //    handlerImageSaver!!.removeCallbacksAndMessages(null)
    }

}