package ru.android.hikanumaruapp.ui.reader

import android.os.Handler
import android.util.Log
import android.widget.ImageView
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bumptech.glide.Glide
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.catch
import ru.android.hikanumaruapp.provider.ReaderProvider
import ru.android.hikanumaruapp.utilits.SaverImagePager
import javax.inject.Inject

@HiltViewModel
class ReaderViewModel @Inject constructor(private val provider:ReaderProvider) : ViewModel() {

    private lateinit var job: Job
    private var jobLoadChapterNext: Job? = null
    private var jobLoadChapterPrev: Job? = null
    private var handlerImageSaver: Handler? = null

    private var isLoadNext = false
    private var isLoadPrev = false
    private var isLoadNextUrl = ""
    private var isLoadPrevUrl = ""

    val loadChapter: MutableLiveData<Any> by lazy {
        MutableLiveData<Any>()
    }

    val preloadChapterNext: MutableLiveData<Any> by lazy {
        MutableLiveData<Any>()
    }

    val preloadChapterPrev: MutableLiveData<Any> by lazy {
        MutableLiveData<Any>()
    }

    internal fun getDataChapter(url: String) {
        job = viewModelScope.launch(Dispatchers.IO) {
            provider.preloadChapterFirst(url)
                .catch { exception ->
                    Log.e("ErrorApi", exception.message.toString())
                    // Todo error add
                    //loadNewChapter=true
                    //loadChapterError=true
                }
                .collect {
                    //loadNewChapter=false
                    loadChapter.postValue(it)
                }
        }
    }

    internal fun getDataChapterNext(url: String) {
        if (isLoadNextUrl != url || !jobLoadChapterNext?.isActive!!) {
            isLoadNextUrl = url
            jobLoadChapterNext?.cancel()
            jobLoadChapterNext = viewModelScope.launch(Dispatchers.IO) {
                provider.preloadChapterFirst(url)
                    .catch { exception ->
                        Log.e("ErrorApi", exception.message.toString())
                        // Todo error add
                        //loadNewChapter=true
                        //loadChapterError=true
                    }
                    .collect {
                        //loadNewChapter=false
                        preloadChapterNext.postValue(it)
                    }
            }
        }
    }

    internal fun getDataChapterPrev(url: String) {
        Log.d("ListT2d2", "Load 20 - ${isLoadPrevUrl != url}")
        Log.d("ListT2d2", "Load 20 - ${isLoadPrevUrl }")
        Log.d("ListT2d2", "Load 20 - ${ url}")
        if (isLoadPrevUrl != url ) {
            //if ( !jobLoadChapterPrev?.isActive!!){
            isLoadPrevUrl = url
            //jobLoadChapterPrev?.cancel()
            jobLoadChapterPrev = viewModelScope.launch(Dispatchers.IO) {
                provider.preloadChapterFirst(url)
                    .catch { exception ->
                        Log.e("ErrorApi", exception.message.toString())
                        // Todo error add
                        //loadNewChapter=true
                        //loadChapterError=true
                    }
                    .collect {
                        //loadNewChapter=false
                        preloadChapterPrev.postValue(it)
                    }
            }
       // }
        }
    }






















    fun saveImageChapterInLocal(
        context: FragmentActivity,
        url: String,
        currentItem: Int,
        view: ImageView
    ) {
        handlerImageSaver = Handler()

        job = viewModelScope.launch(Dispatchers.IO) {
            SaverImagePager().saveImage(
                Glide.with(context)
                    .asBitmap()
                    .load(url)
                    .submit()
                    .get(),
                currentItem,
                loadChapter,
                context,
                view,
                handlerImageSaver
            )
        }
    }

    override fun onCleared() {
        super.onCleared()

        if (handlerImageSaver != null)
            handlerImageSaver!!.removeCallbacksAndMessages(null)
    }

}