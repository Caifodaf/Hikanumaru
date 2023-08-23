package ru.android.hikanumaruapp.presentasion.reader.data

import androidx.lifecycle.MutableLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import ru.android.hikanumaruapp.api.init.BaseViewModel
import ru.android.hikanumaruapp.api.init.CoroutinesErrorHandler
import ru.android.hikanumaruapp.data.model.Chapter
import ru.android.hikanumaruapp.data.model.reader.ReaderChapter
import ru.android.hikanumaruapp.presentasion.reader.data.ApplicationSettingsProvider.SourceProvider.MANGALIB_SOURCE
import ru.android.hikanumaruapp.presentasion.reader.data.ApplicationSettingsProvider.SourceProvider.RANOBELIB_SOURCE
import ru.android.hikanumaruapp.presentasion.reader.data.ApplicationSettingsProvider.SourceProvider.READMANGA_SOURCE
import ru.android.hikanumaruapp.presentasion.reader.data.UriSourcesParser.READMANGA_URL
import ru.android.hikanumaruapp.presentasion.reader.viewer.pager.ReaderPagerConst.Companion.NEXT
import javax.inject.Inject

@HiltViewModel
class ParserViewModel @Inject constructor(
    private val readmangaRepository: ReadmangaRepository
    //private val readmangaRepository: ReadmangaRepository
    //private val readmangaRepository: ReadmangaRepository
    ) : BaseViewModel() {

    val creativeworkChaptersList: MutableLiveData<NetworkResponse<MutableList<Chapter>>> by lazy { MutableLiveData<NetworkResponse<MutableList<Chapter>>>() }

    val chapterReader: MutableLiveData<NetworkResponse<ReaderChapter>> by lazy { MutableLiveData<NetworkResponse<ReaderChapter>>() }
    val chapterReaderPrev: MutableLiveData<NetworkResponse<ReaderChapter>> by lazy { MutableLiveData<NetworkResponse<ReaderChapter>>() }
    val chapterReaderNext: MutableLiveData<NetworkResponse<ReaderChapter>> by lazy { MutableLiveData<NetworkResponse<ReaderChapter>>() }

    fun getChaptersList(source: String?, url: String, coroutinesErrorHandler: CoroutinesErrorHandler
    ) {
        baseRequest(creativeworkChaptersList, coroutinesErrorHandler) {
            when (source) {
                READMANGA_SOURCE -> readmangaRepository.getChaptersList(READMANGA_URL.dropLast(1) +url)
                //MANGALIB_SOURCE -> null
                //RANOBELIB_SOURCE -> null
                else -> readmangaRepository.getChaptersList(READMANGA_URL.dropLast(1) + url)
            }
        }
    }

    fun getChapterReader(source: String?, url: String, coroutinesErrorHandler: CoroutinesErrorHandler
    ) {
        baseRequest(chapterReader, coroutinesErrorHandler) {
            when (source) {
                READMANGA_SOURCE -> readmangaRepository.getChapterReader(url)
                //MANGALIB_SOURCE -> null
                //RANOBELIB_SOURCE -> null
                else -> readmangaRepository.getChapterReader(url)
            }
        }
    }

    fun getPreloadChapterReader(step: Int,source: String?, url: String, coroutinesErrorHandler: CoroutinesErrorHandler
    ) {
        baseRequest(
            if (step == NEXT) chapterReaderNext else chapterReaderPrev,
            coroutinesErrorHandler) {
            when (source) {
                READMANGA_SOURCE -> readmangaRepository.getChapterReader(url)
                //MANGALIB_SOURCE -> null
                //RANOBELIB_SOURCE -> null
                else -> readmangaRepository.getChapterReader(url)
            }
        }
    }

}