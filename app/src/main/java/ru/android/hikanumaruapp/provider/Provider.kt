package ru.android.hikanumaruapp.provider

import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.android.hikanumaruapp.model.Chapter
import ru.android.hikanumaruapp.model.Manga
import ru.android.hikanumaruapp.model.ResultDataFirst
import ru.android.hikanumaruapp.provider.SourceProvider.MANGALIB_SOURCE
import ru.android.hikanumaruapp.provider.SourceProvider.RANOBELIB_SOURCE
import ru.android.hikanumaruapp.provider.SourceProvider.READMANGA_SOURCE
import ru.android.hikanumaruapp.provider.SourceProvider.SELECTED_SOURCE
import ru.android.hikanumaruapp.ui.reader.model.ReaderChapter
import java.io.IOException
import javax.inject.Inject


class Provider @Inject constructor(){

    private val readmanga = ReadMangaParserProvider()
    private var source: Int = 0

    private fun checkSource(){
        //todo add fun
        source = SELECTED_SOURCE
    }

    fun downloadMangaPage(url: String, list: MutableList<Manga>): Flow<MutableList<Manga>> {
        return when (source) {
            READMANGA_SOURCE -> loadDataMangaPage(url, list)
            MANGALIB_SOURCE -> flow { emit(list) }
            RANOBELIB_SOURCE -> flow { emit(list) }
            else -> flow { emit(list) }
        }
    }

    fun downloadMangaPageChapters(url: String, list: MutableList<Chapter>): Flow<MutableList<Chapter>> {
        return when (source) {
            READMANGA_SOURCE -> loadDataMangaPageChapters(url, list)
            MANGALIB_SOURCE -> flow { emit(list) }
            RANOBELIB_SOURCE -> flow { emit(list) }
            else -> flow { emit(list) }
        }
    }


    private fun loadDataMangaPage(url: String, list: MutableList<Manga>): Flow<MutableList<Manga>> =
        readmanga.getDatMangaPage(url, list)

    private fun loadDataMangaPageChapters(url: String, list: MutableList<Chapter>): Flow<MutableList<Chapter>> =
        readmanga.getDatMangaChapters(url, list)

}