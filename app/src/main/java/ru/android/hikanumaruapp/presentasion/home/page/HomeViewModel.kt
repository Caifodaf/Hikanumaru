package ru.android.hikanumaruapp.presentasion.home.page

import android.util.Log
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.common.util.ArrayUtils.contains
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import ru.android.hikanumaruapp.api.api.main.MainApiViewModel
import ru.android.hikanumaruapp.utilits.recyclerviews.RecyclerViewClickListener
import ru.android.hikanumaruapp.api.init.ApiResponse
import ru.android.hikanumaruapp.api.init.CoroutinesErrorHandler
import ru.android.hikanumaruapp.api.models.CreativeWorksRequestModel
import ru.android.hikanumaruapp.api.models.ErrorResponse
import ru.android.hikanumaruapp.data.model.*
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(): ViewModel(), RecyclerViewClickListener {

    val error: MutableLiveData<ErrorResponse> by lazy { MutableLiveData<ErrorResponse>() }

    val listHistoryData: MutableLiveData<List<MangaPageTextDate>> by lazy { MutableLiveData<List<MangaPageTextDate>>() }

    val mainGenresResponse: MutableLiveData<ApiResponse<List<GenresMainModel>>> by lazy { MutableLiveData<ApiResponse<List<GenresMainModel>>>() }
    val mainMangaResponse: MutableLiveData<ApiResponse<List<MangaList>>> by lazy { MutableLiveData<ApiResponse<List<MangaList>>>() }
    val mainManhvaResponse: MutableLiveData<ApiResponse<List<MangaList>>> by lazy { MutableLiveData<ApiResponse<List<MangaList>>>() }
    val mainPopularResponse: MutableLiveData<ApiResponse<List<MangaList>>> by lazy { MutableLiveData<ApiResponse<List<MangaList>>>() }

    val mainGenresList: MutableLiveData<List<GenresMainModel>> by lazy { MutableLiveData<List<GenresMainModel>>() }
    val mainMangaList: MutableLiveData<List<MangaList>> by lazy { MutableLiveData<List<MangaList>>() }
    val mainManhvaList: MutableLiveData<List<MangaList>> by lazy { MutableLiveData<List<MangaList>>() }
    val mainPopularList: MutableLiveData<List<MangaList>> by lazy { MutableLiveData<List<MangaList>>() }

    private lateinit var job: Job

    internal fun getMainPage(vmMain: MainApiViewModel, block: String) {
        when(block) {
            "Genres" ->
                vmMain.getMainPageGenres(mainGenresResponse,object: CoroutinesErrorHandler {
                        override fun onError(cause: Throwable?, message: String) {
                            when(cause.toString().substringBefore(':')){
                                "java.net.SocketTimeoutException" -> error.postValue(ErrorResponse(504, message.toString()))
                                "java.net.UnknownHostException"  ->  error.postValue(ErrorResponse(-1, message.toString()))
                                else  ->  error.postValue(ErrorResponse(502, message.toString()))
                            }
                        }
                    })
            "Manga" ->
                vmMain.getCreativeWorksCollection(mainMangaResponse,
                    CreativeWorksRequestModel(type = listOf("manga")),
                    object: CoroutinesErrorHandler {
                        override fun onError(cause: Throwable?, message: String) {
                            when(cause.toString().substringBefore(':')){
                                "java.net.SocketTimeoutException" -> error.postValue(ErrorResponse(504, message.toString()))
                                "java.net.UnknownHostException"  ->  error.postValue(ErrorResponse(-1, message.toString()))
                                else  ->  error.postValue(ErrorResponse(502, message.toString()))
                            }
                        }
                    })
            "Manhva" ->
                vmMain.getCreativeWorksCollection(mainManhvaResponse,
                    CreativeWorksRequestModel(type = listOf("manhva")),
                    object: CoroutinesErrorHandler {
                        override fun onError(cause: Throwable?, message: String) {
                            when(cause.toString().substringBefore(':')){
                                "java.net.SocketTimeoutException" -> error.postValue(ErrorResponse(504, message.toString()))
                                "java.net.UnknownHostException"  ->  error.postValue(ErrorResponse(-1, message.toString()))
                                else  ->  error.postValue(ErrorResponse(502, message.toString()))
                            }
                        }
                    })
        }
    }


    //private fun getLoad() {
    //    job = Coroutines.ioThenMain(
    //        { dataDebug.setHistoryHome(listHistoryLoad) },
    //        {
    //            _listHistory.value = listHistoryLoad
    //            historyAdapter.setMain(listHistoryLoad)
    //        }
    //    )
    //    job = Coroutines.ioThenMain(
    //        { dataDebug.setPopularHome(listPopularLoad) },
    //        {
    //            _listPopular.value = listPopularLoad
    //            popularAdapter.setMain(listPopularLoad)
    //        }
    //    )
    //}

    internal fun doubleScrollGenres (
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

    override fun onRecyclerViewItemClick(view: View, list: Any?) {}

    override fun onCleared() {
        if(::job.isInitialized) job.cancel()
        super.onCleared()
    }
}