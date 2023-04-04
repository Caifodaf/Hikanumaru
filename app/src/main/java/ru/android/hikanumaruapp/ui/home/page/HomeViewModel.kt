package ru.android.hikanumaruapp.ui.home.page

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import ru.android.hikanumaruapp.utilits.recyclerviews.RecyclerViewClickListener
import ru.android.hikanumaruapp.utilits.recyclerviews.SpaceItemDecoration
import ru.android.hikanumaruapp.R
import ru.android.hikanumaruapp.data.debug.DataSetDebug
import ru.android.hikanumaruapp.model.*
import ru.android.hikanumaruapp.ui.home.page.adapters.*
import ru.android.hikanumaruapp.utilits.Coroutines
import ru.android.hikanumaruapp.utilits.navigation.Events
import ru.android.hikanumaruapp.utilits.navigation.NavigationFragmentinViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(): ViewModel(), RecyclerViewClickListener {

    init {
        Log.d("testCrated", "HomeViewModel create")
    }

    private lateinit var job: Job
    val emitter = Events.Emitter()
    private var dataDebug = DataSetDebug()

    private val historyAdapter = HomeHistoryMangaAdapter(this)
    private val genresAdapter = HomeGenresMangaAdapter(this)
    private val genres2Adapter = HomeGenresMangaAdapter(this)
    private val specialAdapter = HomeSpecialMangaAdapter(this)
    private val journalAdapter = HomeJournalMangaAdapter(this)
    private val newsAdapter = HomeNewsMangaAdapter(this)
    private val popularAdapter = HomePopularMangaAdapter(this)

    private val listHistoryLoad = mutableListOf<MangaMainModel>()
    private val listGenresLoad = mutableListOf<GenresMainModel>()
    private val listGenresTwoLoad = mutableListOf<GenresMainModel>()
    private val listSpecialLoad = mutableListOf<MangaMainModel>()
    private val listJournalsLoad = mutableListOf<JournalMainModel>()
    private val listNewsLoad = mutableListOf<MangaMainModel>()
    private val listPopularLoad = mutableListOf<MangaPopularMainModel>()

    private val _listHistory = MutableLiveData<List<MangaMainModel>>()
    val listHistory: LiveData<List<MangaMainModel>>
        get() = _listHistory

    private val _listGenres = MutableLiveData<List<GenresMainModel>>()
    val listGenres: LiveData<List<GenresMainModel>>
        get() = _listGenres

    private val _listGenresTwo = MutableLiveData<List<GenresMainModel>>()
    val listGenresTwo: LiveData<List<GenresMainModel>>
        get() = _listGenresTwo

    private val _listSpecial = MutableLiveData<List<MangaMainModel>>()
    val listSpecial: LiveData<List<MangaMainModel>>
        get() = _listSpecial

    private val _listJournals = MutableLiveData<List<JournalMainModel>>()
    val listJournals: LiveData<List<JournalMainModel>>
        get() = _listJournals

    private val _listNews = MutableLiveData<List<MangaMainModel>>()
    val listNews: LiveData<List<MangaMainModel>>
        get() = _listNews

    private val _listPopular = MutableLiveData<List<MangaPopularMainModel>>()
    val listPopular: LiveData<List<MangaPopularMainModel>>
        get() = _listPopular

    init{
        getLoad()
    }


    private fun getLoad() {
        job = Coroutines.ioThenMain(
            { dataDebug.setHistoryHome(listHistoryLoad) },
            {
                _listHistory.value = listHistoryLoad
                historyAdapter.setMain(listHistoryLoad)
            }
        )
        job = Coroutines.ioThenMain(
            { dataDebug.setGenresHome(listGenresLoad) },
            {
                _listGenres.value = listGenresLoad
                genresAdapter.setMain(listGenresLoad)
            }
        )
        job = Coroutines.ioThenMain(
            { dataDebug.setGenres2Home(listGenresTwoLoad) },
            {
                _listGenresTwo.value = listGenresTwoLoad
                genres2Adapter.setMain(listGenresTwoLoad)
            }
        )
        job = Coroutines.ioThenMain(
            { dataDebug.setSpecialHome(listSpecialLoad) },
            {
                _listSpecial.value = listSpecialLoad
                specialAdapter.setMain(listSpecialLoad)
            }
        )
        job = Coroutines.ioThenMain(
            { dataDebug.setJournalHome(listJournalsLoad) },
            {
                _listJournals.value = listJournalsLoad
                journalAdapter.setMain(listJournalsLoad)
            }
        )
        job = Coroutines.ioThenMain(
            { dataDebug.setNewHome(listNewsLoad) },
            {
                _listNews.value = listNewsLoad
                newsAdapter.setMain(listNewsLoad)
            }
        )
        job = Coroutines.ioThenMain(
            { dataDebug.setPopularHome(listPopularLoad) },
            {
                _listPopular.value = listPopularLoad
                popularAdapter.setMain(listPopularLoad)
            }
        )
    }

    fun initRecyclerView(recyclerView: RecyclerView,
        scrollListenersGenres: Array<RecyclerView.OnScrollListener?> = emptyArray()
    ) {
        val snapHelper = LinearSnapHelper() // Or PagerSnapHelper

        if (recyclerView.id != R.id.rv_popular_list)
            recyclerView.addItemDecoration(SpaceItemDecoration())

        when (recyclerView.id) {
            R.id.rv_history_list -> {
                snapHelper.attachToRecyclerView(recyclerView)
                recyclerView.adapter = historyAdapter
            }
            R.id.rv_genres_list -> {
                recyclerView.addOnScrollListener(scrollListenersGenres[0]!!)
                recyclerView.adapter = genresAdapter
            }
            R.id.rv_genres_list2 -> {
                recyclerView.addOnScrollListener(scrollListenersGenres[1]!!)
                recyclerView.adapter = genres2Adapter
            }
            R.id.rv_special_list -> {
                snapHelper.attachToRecyclerView(recyclerView)
                recyclerView.adapter = specialAdapter
            }
            R.id.rv_journal_list -> {
                recyclerView.adapter = journalAdapter
            }
            R.id.rv_new_list -> {
                snapHelper.attachToRecyclerView(recyclerView)
                recyclerView.adapter = newsAdapter
            }
            R.id.rv_popular_list -> recyclerView.adapter = popularAdapter
        }
    }

    fun doubleScrollGenres (
        rv_list: RecyclerView,
        rv_list2: RecyclerView,
        scrollListenersGenres: Array<RecyclerView.OnScrollListener?>
    ): Array<RecyclerView.OnScrollListener?> {
        scrollListenersGenres[0] = object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                rv_list2.removeOnScrollListener(scrollListenersGenres[1]!!)
                rv_list2.scrollBy(dx, dy)
                rv_list2.addOnScrollListener(scrollListenersGenres[1]!!)
            }
        }
        scrollListenersGenres[1] = object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                rv_list.removeOnScrollListener(scrollListenersGenres[0]!!)
                rv_list.scrollBy(dx, dy)
                rv_list.addOnScrollListener(scrollListenersGenres[0]!!)
            }
        }
        return scrollListenersGenres
    }

    override fun onRecyclerViewItemClick(view: View, list: Any?) {
        val bundle = Bundle()

        when (view.id) {
            R.id.rl_block_manga_main_item -> {
                bundle.putString("url", (list as MangaMainModel).linkPage)
                emitter.emitAndExecute(NavigationFragmentinViewModel.NavigationFrag(
                    R.id.action_navigation_home_to_navigation_mangapage, bundle))
            }
            R.id.rl_back_genres_item -> {
                Log.d("testCrated", "rl_back_genres_item click $list")
            }
            R.id.rl_back_journal_item -> {
                Log.d("testCrated", "rl_back_genres_item click")
            }
            R.id.CCMainLargeMangaBlock -> {
                bundle.putString("url", (list as MangaPopularMainModel).linkPage)
                    emitter.emitAndExecute(NavigationFragmentinViewModel.NavigationFrag(
                    R.id.action_navigation_home_to_navigation_mangapage, bundle))
            }
        }
    }

    override fun onCleared() {
        if(::job.isInitialized) job.cancel()
        super.onCleared()
    }
}