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

    fun preloadChapterFirst(url: String): Flow<Any> =
         when (source) {
            READMANGA_SOURCE -> loadChapterFirst(url)
            MANGALIB_SOURCE -> flow { emit(Any()) }
            RANOBELIB_SOURCE -> flow { emit(Any()) }
            else -> flow { emit(Any()) }
        }

    private fun loadChapterFirst(url: String): Flow<Any> =
        readmanga.getDataMangaChapterReader(url)

}