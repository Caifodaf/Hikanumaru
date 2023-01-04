package ru.android.hikanumaruapp.ui.reader

import android.annotation.SuppressLint
import android.os.Handler
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.catch
import ru.android.hikanumaruapp.R
import ru.android.hikanumaruapp.model.Chapter
import ru.android.hikanumaruapp.provider.ReaderProvider
import ru.android.hikanumaruapp.ui.reader.model.ReaderChapter
import ru.android.hikanumaruapp.utilits.SaverImagePager
import javax.inject.Inject

@HiltViewModel
class ReaderViewModel @Inject constructor(private val provider:ReaderProvider) : ViewModel() {

    private lateinit var job: Job
    private var jobLoadChapterNext: Job? = null
    private var jobLoadChapterPrev: Job? = null
    private var handlerImageSaver: Handler? = null

    var lastDirSaveImage: String = ""

    val loadChapter: MutableLiveData<ReaderChapter> by lazy {
        MutableLiveData<ReaderChapter>()
    }

    val preloadChapterNext: MutableLiveData<ReaderChapter> by lazy {
        MutableLiveData<ReaderChapter>()
    }

    val preloadChapterPrev: MutableLiveData<ReaderChapter> by lazy {
        MutableLiveData<ReaderChapter>()
    }

    val chapterList: MutableLiveData<MutableList<Chapter>> by lazy {
        MutableLiveData<MutableList<Chapter>>()
    }

    fun getDataChapter(url: String) {
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

    val checkLoadChapterPrev:Boolean =
        jobLoadChapterPrev == null || jobLoadChapterPrev!!.isActive
    val checkLoadChapterNext:Boolean =
        jobLoadChapterNext == null || jobLoadChapterNext!!.isActive

    fun getDataChapterNext(url: String) {
        if (jobLoadChapterNext == null || jobLoadChapterNext!!.isActive) {
            jobLoadChapterNext = viewModelScope.launch(Dispatchers.IO) {
                provider.preloadChapterFirst(url)
                    .catch { exception ->
                        Log.e("ErrorApi", exception.message.toString())
                        // Todo error add
                        //loadNewChapter=true
                        //loadChapterError=true
                    }
                    .collect {
                        preloadChapterNext.postValue(it)
                    }
            }
        }
    }

    fun getDataChapterPrev(url: String) {
        jobLoadChapterPrev = viewModelScope.launch(Dispatchers.IO) {
            provider.preloadChapterFirst(url)
                .catch { exception ->
                    Log.e("ErrorApi", exception.message.toString())
                    // Todo error add
                    //loadNewChapter=true
                    //loadChapterError=true
                }
                .collect {
                    preloadChapterPrev.postValue(it)
                }
        }
    }

    fun reloadChapterList(url: String) {
        job = viewModelScope.launch(Dispatchers.IO) {
            provider.downloadReaderPageChapters(url)
                .catch { exception ->
                    Log.e("ErrorApi", exception.message.toString())
                    // Todo error add
                    //loadNewChapter=true
                    //loadChapterError=true
                }
                .collect {
                    chapterList.postValue(it)
                }
        }
    }






















    @SuppressLint("WrongConstant", "UseCompatLoadingForDrawables",
        "UseCompatLoadingForColorStateLists")
    fun saveImageChapterInLocal(
        context: FragmentActivity,
        url: String,
        currentItem: Int,
        view: ImageView,
    ) {
        handlerImageSaver = Handler()

        job = viewModelScope.launch(Dispatchers.IO) {
            when( SaverImagePager(
                url, currentItem, loadChapter,this@ReaderViewModel).saveImage()){
                true -> {
                    context.runOnUiThread {
                        Toast.makeText(context, "Страница сохранена.\n$lastDirSaveImage", 1000)
                            .show()

                        view.setImageDrawable(context.resources.getDrawable(R.drawable.ic_check_circle_on))
                        view.imageTintList = context.resources.getColorStateList(R.color.white)
                        view.setBackgroundResource(R.drawable.gradient_green)
                        view.backgroundTintList = context.resources.getColorStateList(R.color.green)

                        handlerImageSaver!!.postDelayed(Runnable {
                            view.setImageDrawable(context.resources.getDrawable(R.drawable.ic_reader_image_download))
                            view.imageTintList =
                                context.resources.getColorStateList(R.color.black_back_text)
                            view.setBackgroundResource(R.drawable.rectangle_rounded_all)
                            view.backgroundTintList =
                                context.resources.getColorStateList(R.color.black_back)
                        }, 2500)
                    }
                }
                false -> {
                    context.runOnUiThread {
                        Toast.makeText(context, "Страница не сохранена.", Toast.LENGTH_LONG).show()
                    }
                }

            }
        }
    }

    override fun onCleared() {
        super.onCleared()

        if (handlerImageSaver != null)
            handlerImageSaver!!.removeCallbacksAndMessages(null)
    }

}