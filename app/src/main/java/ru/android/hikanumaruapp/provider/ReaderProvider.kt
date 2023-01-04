package ru.android.hikanumaruapp.provider

import android.util.Log
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import ru.android.hikanumaruapp.model.Chapter
import ru.android.hikanumaruapp.model.Manga
import ru.android.hikanumaruapp.model.ResultDataFirst
import ru.android.hikanumaruapp.provider.SourceProvider.MANGALIB_SOURCE
import ru.android.hikanumaruapp.provider.SourceProvider.RANOBELIB_SOURCE
import ru.android.hikanumaruapp.provider.SourceProvider.READMANGA_SOURCE
import ru.android.hikanumaruapp.provider.SourceProvider.SELECTED_SOURCE
import ru.android.hikanumaruapp.ui.reader.model.ReaderChapter
import ru.android.hikanumaruapp.ui.reader.model.ReaderChapterPage
import java.io.IOException
import javax.inject.Inject

class ReaderProvider @Inject constructor(){
    private val readmanga = ReadMangaParserProvider()

    private var source: Int = 0

    private fun checkSource(){
        //todo add fun
        source = SELECTED_SOURCE
    }

    fun preloadChapterFirst(url: String): Flow<ReaderChapter> =
         when (source) {
            READMANGA_SOURCE -> loadChapterFirst(url)
            MANGALIB_SOURCE -> flow { emit(ReaderChapter()) }
            RANOBELIB_SOURCE -> flow { emit(ReaderChapter()) }
            else -> flow { emit(ReaderChapter()) }
        }

    private fun loadChapterFirst(url: String): Flow<ReaderChapter> =
        readmanga.getDataMangaChapterReader(url)

    fun downloadReaderPageChapters(url: String): Flow<MutableList<Chapter>> {
        return when (source) {
            READMANGA_SOURCE -> loadDataReaderPageChapters(url)
            MANGALIB_SOURCE -> flow { emit(listOf<Chapter>() as MutableList<Chapter>) }
            RANOBELIB_SOURCE -> flow { emit(listOf<Chapter>() as MutableList<Chapter>) }
            else -> flow { emit(listOf<Chapter>() as MutableList<Chapter>) }
        }
    }

    private fun loadDataReaderPageChapters(url: String): Flow<MutableList<Chapter>> =
        readmanga.getDataReaderChapters(url)
}