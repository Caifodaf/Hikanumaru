package ru.android.hikanumaruapp.presentasion.mangapage.chapterspage

import android.content.Context
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import ru.android.hikanumaruapp.R
import ru.android.hikanumaruapp.model.Chapter
import ru.android.hikanumaruapp.provider.Provider
import ru.android.hikanumaruapp.presentasion.mangapage.adapter.MangaPageChapterAdapter
import ru.android.hikanumaruapp.utilits.navigation.Events
import ru.android.hikanumaruapp.utilits.recyclerviews.RecyclerViewClickListener
import javax.inject.Inject


@HiltViewModel
class ChaptersPageViewModel @Inject constructor() : ViewModel(), RecyclerViewClickListener {

    private lateinit var job: Job
    private val provider = Provider()
    val emitter = Events.Emitter()

    private var isReversedList = false
    private var isFirstLoad = true
    var isLoadChapter = false


    private val chapterAdapter = MangaPageChapterAdapter( this)

    private val listChaptersLoad = mutableListOf<Chapter>()

    private val _listChapters = MutableLiveData<List<Chapter>>()
    val listChapters: LiveData<List<Chapter>>
        get() = _listChapters

    private val _errorView = MutableLiveData<Boolean>()
    val errorView: LiveData<Boolean>
        get() = _errorView

    fun setListChaptersData(list: MutableList<Chapter>, count: Int = 0 ) {
        _listChapters.value = list
        if (count+1 == list.size)
            chapterAdapter.setMain(list)
    }

    fun initRecyclerView(recyclerView: RecyclerView, llmRecycler: LinearLayoutManager) {
        when (recyclerView.id) {
            R.id.rl_chapter_list_tab -> {
                recyclerView.layoutManager = llmRecycler

                //val snapHelper = LinearSnapHelper() // Or PagerSnapHelper
                //val layoutManager = recyclerView.layoutManager
                //val snapView = snapHelper.findSnapView(layoutManager)
                //val snapPosition = layoutManager!!.getPosition(snapView!!)
                //val behavior = Behavior.NOTIFY_ON_SCROLL
//
                //recyclerView.attachSnapHelperWithListener(snapHelper, behavior, onSnapPositionChangeListener = )

                recyclerView.adapter = chapterAdapter
            }
        }
    }

    //fun reLoadChapter(url: String) {
    //    if (listChaptersLoad.isNullOrEmpty()) {
    //        try {
    //            if (isLoadChapter || isFirstLoad || !job.isActive) {
    //                isLoadChapter = false
    //                isFirstLoad = false
    //                listChaptersLoad.clear()
//
    //                job = Coroutines.ioThenMain(
    //                    { provider.downloadMangaPageChapters(url, listChaptersLoad) },
    //                    {
    //                        _listChapters.value = listChaptersLoad
    //                        chapterAdapter.setMain(listChaptersLoad)
    //                    }
    //                )
    //            }
    //        } catch (e: IOException) {
    //            _errorView.value = true
    //        }
    //    }
    //}

    fun btnReverseChapterList(button: LinearLayout, context: Context? = null, view: Any? = null){
        button.setOnClickListener {
            if(!_listChapters.value.isNullOrEmpty()) {
                val list:MutableList<Chapter> = listChapters.value!!.toMutableList()
                list.reverse()
                when(isReversedList){
                    true->{
                        (view as ImageView).setImageDrawable(context!!.resources.getDrawable(R.drawable.ic_sort_arrow_l))
                        false}
                    false-> {
                        (view as ImageView).setImageDrawable(context!!.resources.getDrawable(R.drawable.ic_sort_arrow_r))
                        true }
                }.also { isReversedList = it }
                chapterAdapter.setMain(list)
            }
        }
    }

    override fun onRecyclerViewItemClick(view: View, list: Any?) {
        when (view.id) {
            R.id.CCMainBlockChapter -> {
                //val toInfoReader: Intent = Intent(activity, ReaderActivity::class.java)
                //toInfoReader.putExtra("url",url)
                //toInfoReader.putExtra("title",nameTitle)

                //var load = true
                //if(isLoadChapter){
                //    if(chapterList.size == countChapters.toInt()) {
                //        isLoadChapter = false
                //        load = true
                //    }else{
                //        jobDataChapter.cancel()
                //        load = false
                //    }
                //}

                //val str = Gson().toJson(chapterList)
                //toInfoReader.putExtra("list", str)
                //toInfoReader.putExtra("checkLoadList", load)
                //toInfoReader.putExtra("reverse", isReversedList)
                //toInfoReader.putExtra("urlPage", url)
                //toInfoReader.putExtra("count", (chapterList.size).toString())

                //startActivity(toInfoReader)
            }
        }
    }

    override fun onCleared() {
        if(::job.isInitialized) job.cancel()
        super.onCleared()
    }



}