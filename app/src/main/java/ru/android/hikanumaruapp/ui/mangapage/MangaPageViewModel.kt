package ru.android.hikanumaruapp.ui.mangapage

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import ru.android.hikanumaruapp.R
import ru.android.hikanumaruapp.ui.mangapage.adapter.MangaPageTextAdapter
import ru.android.hikanumaruapp.model.*
import ru.android.hikanumaruapp.provider.Provider
import ru.android.hikanumaruapp.ui.mangapage.adapter.MangaPageChapterAdapter
import ru.android.hikanumaruapp.utilits.*
import javax.inject.Inject

@HiltViewModel
class MangaPageViewModel @Inject constructor() : ViewModel(), RecyclerViewClickListener, UIUtils {

    private lateinit var job: Job
    private val provider = Provider()
    val emitter = Events.Emitter()

    private var isReversedList = false
    private var isLoadingChapterList = false

    var urlPage: String = ""

    private val textAdapter = MangaPageTextAdapter( this)
    private val chapterAdapter = MangaPageChapterAdapter( this)

    private val listPageLoad = mutableListOf<Manga>()
    private val listChaptersLoad = mutableListOf<Chapter>()
    private val listTextDataLoad = mutableListOf<MangaPageTextDate>()

    private val _listPage = MutableLiveData<List<Manga>>()
    val listPage: LiveData<List<Manga>>
        get() = _listPage

    private val _listChapters = MutableLiveData<List<Chapter>>()
    val listChapters: LiveData<List<Chapter>>
        get() = _listChapters

    private val _listTextData = MutableLiveData<List<MangaPageTextDate>>()
    val listTextData: LiveData<List<MangaPageTextDate>>
        get() = _listTextData


    init {

    }

    //getDataPage
    //getDatMangaPage
    fun getDataPage(urlPage: String) {
        job = viewModelScope.launch(Dispatchers.IO) {
            provider.downloadMangaPage(urlPage, listPageLoad)
                .catch { exception ->
                    Log.e("ErrorApi", exception.message.toString())
                    // Todo error add
                }
                .collect {
                    _listPage.postValue(it.requireNoNulls())
                }
        }
    }
    //getDataPageChapter
    //getDatMangaChapters
    fun getDataChapter(urlPage: String) {
        isLoadingChapterList = true
        job = viewModelScope.launch(Dispatchers.IO) {
            provider.downloadMangaPageChapters(urlPage, listChaptersLoad)
                .catch { exception ->
                    Log.e("ErrorApi", exception.message.toString())
                    // Todo error add
                }
                .collect {
                    isLoadingChapterList = false
                    _listChapters.postValue(it.requireNoNulls())
                    _listPage.value!![0].chapter = listChaptersLoad
                }
        }
    }

    fun setListTextData(list: MutableList<MangaPageTextDate>) {
        _listTextData.value = list
        textAdapter.setMain(list)
    }

    fun setListChapterData(list: MutableList<Chapter>) {
        _listChapters.value = list
        chapterAdapter.setMain(list)
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    fun initBtn(button: LinearLayout,context: Context? = null,view: Any? = null){
        timerDoubleBtn(button)
        button.setOnClickListener{
            when (button.id) {
                // Sorting btn
                R.id.ll_sort_btn -> {
                    if (!_listPage.value!![0].chapter.isNullOrEmpty()) {
                        if (!_listPage.value!![0].chapter!![0].notChapter) {
                            val list: MutableList<Chapter> = _listPage.value!![0].chapter!!
                            list.reverse()
                            when (isReversedList) {
                                true -> {
                                    (view as ImageView).setImageDrawable(context!!.resources.getDrawable(
                                        R.drawable.ic_sort_arrow_l))
                                    false
                                }
                                false -> {
                                    (view as ImageView).setImageDrawable(context!!.resources.getDrawable(
                                        R.drawable.ic_sort_arrow_r))
                                    true
                                }
                            }.also { isReversedList = it }
                            chapterAdapter.setMain(list)
                        }
                    }
                }
                // See more chapter nav btn
                R.id.ll_btn_more_chapter_list -> {
                    val bundle = Bundle()
                    val model = _listPage.value!![0]

                    val str = Gson().toJson(model.chapter!!)
                    bundle.putString("list", str)
                    bundle.putBoolean("checkLoadList", isLoadingChapterList)
                    bundle.putBoolean("reverse", isReversedList)
                    bundle.putString("name", model.name)
                    bundle.putString("url", urlPage)
                    bundle.putString("count", (model.chapterCount?.minus(1)).toString())

                    emitter.emitAndExecute(NavigationFragmentinViewModel.NavigationFrag(
                        R.id.navigation_chapters_page, bundle))
                }
            }
        }
    }

    fun initRecyclerView(recyclerView: RecyclerView, ) {
        when (recyclerView.id) {
            R.id.scroll_text_block -> {
                recyclerView.addItemDecoration(SpaceItemDecoration(8))
                recyclerView.adapter = textAdapter
            }
            R.id.rv_chapters_manga ->
                recyclerView.adapter = chapterAdapter
        }
    }

    override fun onRecyclerViewItemClick(view: View, list: Any?) {
        val bundle = Bundle()

        when (view.id) {
            R.id.rl_block_manga_main_item -> {
                //bundle.putString("url", (list as MangaMainModel).linkPage)
                emitter.emitAndExecute(
                    NavigationFragmentinViewModel.NavigationFrag(
                    R.id.navigation_mangapage, bundle))
            }
        }
    }

    override fun onCleared() {
        if(::job.isInitialized) job.cancel()
        super.onCleared()
    }
}