package ru.android.hikanumaruapp.ui.mangapage

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.catch
import ru.android.hikanumaruapp.R
import ru.android.hikanumaruapp.local.storage.library.LibraryBase
import ru.android.hikanumaruapp.ui.mangapage.adapter.MangaPageTextAdapter
import ru.android.hikanumaruapp.model.*
import ru.android.hikanumaruapp.provider.Provider
import ru.android.hikanumaruapp.ui.mangapage.adapter.MangaPageChapterAdapter
import ru.android.hikanumaruapp.utilits.*
import ru.android.hikanumaruapp.utilits.navigation.Events
import ru.android.hikanumaruapp.utilits.navigation.NavigationFragmentinViewModel
import ru.android.hikanumaruapp.utilits.recyclerviews.RecyclerViewClickListener
import ru.android.hikanumaruapp.utilits.recyclerviews.SpaceItemDecoration
import ru.android.hikanumaruapp.utilits.room.ConvertersRoom
import ru.android.hikanumaruapp.utilits.room.GsonParser
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class MangaPageViewModel @Inject constructor(private val provider:Provider) : ViewModel(),
    RecyclerViewClickListener, UIUtils {

    private lateinit var job: Job
    private lateinit var jobBookmark: Job
    private lateinit var jobLibrary: Job
    private var libraryDB : LibraryBase? = null

    val library: MutableLiveData<List<Manga>> by lazy { MutableLiveData<List<Manga>>() }
    val isBookmark: MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>() }

    val emitter = Events.Emitter()

    private var isReversedList = false
    private var isLoadingChapterList = false

    var urlPage: String = ""

    private val textAdapter = MangaPageTextAdapter( this)
    private val chapterAdapter = MangaPageChapterAdapter( this)

    private val listPageLoad = mutableListOf<Manga>()
    private val listChaptersLoad = mutableListOf<Chapter>()
    private val listTextDataLoad = mutableListOf<MangaPageTextDate>()

    val listPage: MutableLiveData<Manga> by lazy { MutableLiveData<Manga>() }
    val listChapters: MutableLiveData<List<Chapter>> by lazy { MutableLiveData<List<Chapter>>() }
    val listTextData: MutableLiveData<List<MangaPageTextDate>> by lazy { MutableLiveData<List<MangaPageTextDate>>() }

    init {

    }

    fun initBDLibrary(context: FragmentActivity) {
        if (libraryDB == null) {
            try {
                val roomConverter = ConvertersRoom(GsonParser(Gson()))
                libraryDB = Room.databaseBuilder(
                    context.applicationContext,
                    LibraryBase::class.java, "manga-library.db"
                ).addTypeConverter(roomConverter)
                    .build()
                checkBookmarkLibrary()
            }catch (e: Exception){
                Log.e("ErrorDB", "Library $e")
            }
        }
    }

    private fun checkBookmarkLibrary() {
        jobLibrary = Coroutines.ioThenMain({
            try {
                libraryDB!!.LibraryDao().getById(listPage.value!!.id)
            } catch (e: Exception) {
                Log.e("ErrorBD", "Library.getById(id) - " + e.message.toString())
                null
            }
        },
            {
                when (it) {
                    null -> isBookmark.postValue(false)
                    else -> isBookmark.postValue(true)
                }
            }
        )
    }

    fun changeBookmark() {
        if (!::jobBookmark.isInitialized || !jobBookmark.isActive)
            when(isBookmark.value){
                true -> changeBookmarkInBDToFalse()
                false ->  changeBookmarkInBDToTrue()
                else -> changeBookmarkInBDToFalse()
            }
    }

    private fun changeBookmarkInBDToTrue(){
        jobBookmark = Coroutines.ioThenMain(
            {   libraryDB!!.LibraryDao().insertAll(listPage.value!!)
                libraryDB!!.LibraryDao().getById(listPage.value!!.id)
            },
            {
                Log.e("Eadadadadadadadadadadad", "isBookmark ${it}")
                isBookmark.postValue(true) }
        )
    }

    private fun changeBookmarkInBDToFalse(){
        jobBookmark = CoroutineScope(Dispatchers.Main).launch {
            withContext(CoroutineScope(Dispatchers.IO).coroutineContext) {
                libraryDB!!.LibraryDao().delete(listPage.value!!)
            }
            isBookmark.postValue(false)
        }
    }


    fun getDataPage(urlPage: String) {
        job = viewModelScope.launch(Dispatchers.IO) {
            provider.downloadMangaPage(urlPage)
                .catch { exception ->
                    Log.e("ErrorApi", exception.message.toString())
                    // Todo error add
                }
                .collect {
                    listPage.postValue(it)
                }
        }
    }

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
                    listChapters.postValue(it.requireNoNulls())
                    listPage.value!!.chapters = listChaptersLoad
                }
        }
    }

    fun setListTextData(list: MutableList<MangaPageTextDate>) {
        listTextData.value = list
        textAdapter.setMain(list)
    }

    fun setListChapterData(list: MutableList<Chapter>) {
        listChapters.value = list
        chapterAdapter.setMain(list)
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    fun initBtn(button: LinearLayout,context: Context? = null,view: Any? = null){
        button.timerDoubleButton()
        button.setOnClickListener{
            when (button.id) {
                // Sorting btn
                R.id.ll_sort_btn -> {
                    if (!listPage.value!!.chapters.isNullOrEmpty()) {
                        if (!listPage.value!!.chapters!![0].notChapter) {
                            val list: MutableList<Chapter> = listPage.value!!.chapters!!
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
                // See more chapter nav btn todo rework
                R.id.tv_btn_more_chapter_list_ph -> {
                    val bundle = Bundle()
                    val model = listPage.value!!

                    val str = Gson().toJson(model.chapters!!)
                    bundle.putString("list", str)
                    bundle.putBoolean("checkLoadList", isLoadingChapterList)
                    bundle.putBoolean("reverse", isReversedList)
                    bundle.putString("name", model.name)
                    bundle.putString("url", urlPage)
                    bundle.putString("count", (model.chapterCount?.minus(1)).toString())

                    emitter.emitAndExecute(NavigationFragmentinViewModel.NavigationFrag(
                        R.id.action_navigation_mangapage_to_navigation_chapters_page, bundle))
                }
            }
        }
    }

    fun initRecyclerView(recyclerView: RecyclerView ) {
        when (recyclerView.id) {
            //R.id.RVTagsBlock -> {
            //    recyclerView.addItemDecoration(SpaceItemDecoration(8))
            //    recyclerView.adapter = textAdapter
            //}
            R.id.RVChapterList ->
                recyclerView.adapter = chapterAdapter
           // R.id.TVCountChapterStart ->
           //     recyclerView.adapter = chapterAdapter
        }
    }

    fun openReaderPageFastRead(page:Int? = null) {
        val list = listPage.value!!
        val toInfoReader = Bundle()

        // todo add last load reading page
        val urlChapter = if (page != null)
            list.chapters!![page].url.toString()
        else
            list.chapters!!.last().url.toString()


            toInfoReader.putString("url",urlChapter)
            toInfoReader.putString("title",list.name)
            toInfoReader.putString("type",list.type.toString())
            if(!list.chapters.isNullOrEmpty()) {
                val str = Gson().toJson(list.chapters!!)
                toInfoReader.putString("list", str)
            }
            toInfoReader.putString("urlPage", urlPage)
            toInfoReader.putString("count", (list.chapterCount?.minus(1)).toString())

        emitter.emitAndExecute(
            NavigationFragmentinViewModel.NavigationFrag(
                R.id.action_navigation_mangapage_to_navigation_reader, toInfoReader))

    }

    private fun openReaderPageChapter(listHolder: Any?) {
        val toInfoReader = Bundle()

        val listManga = listPage.value!!
        val list: Chapter = listHolder as Chapter

        val urlChapter = list.url.toString()


        toInfoReader.putString("url",urlChapter)
        toInfoReader.putString("title",listManga.name)
        toInfoReader.putInt("type",listManga.type!!.toInt())
        if(!listManga.chapters.isNullOrEmpty()) {
            if (isReversedList){
                val str = Gson().toJson(listManga.chapters!!)
                toInfoReader.putString("list", str)
            }else{
                var list = listManga.chapters!!
                list = list.reversed() as MutableList<Chapter>
                val str = Gson().toJson(list)
                toInfoReader.putString("list", str)
            }
        }
        toInfoReader.putString("urlPage", urlPage)
        toInfoReader.putString("count", (listManga.chapterCount?.minus(1)).toString())

        emitter.emitAndExecute(
            NavigationFragmentinViewModel.NavigationFrag(
                R.id.action_navigation_mangapage_to_navigation_reader, toInfoReader))

    }

    override fun onRecyclerViewItemClick(view: View, list: Any?) {
        when (view.id) {
            R.id.cc_main_block -> {
                (view as ConstraintLayout).timerDoubleButton()
                openReaderPageChapter(list)
            }
//            R.id.rl_block_manga_main_item -> {
//                //bundle.putString("url", (list as MangaMainModel).linkPage)
//                emitter.emitAndExecute(
//                    NavigationFragmentinViewModel.NavigationFrag(
//                    R.id.navigation_mangapage, bundle))
//            }
        }
    }

    override fun onCleared() {
        if(::job.isInitialized) job.cancel()
        super.onCleared()
    }
}