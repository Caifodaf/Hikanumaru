package ru.android.hikanumaruapp.provider

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.android.hikanumaruapp.model.Chapter
import ru.android.hikanumaruapp.model.Manga
import java.io.IOException

class Provider {

    companion object{
        const val READMANGA_SOURCE = 0
        const val MANGALIB_SOURCE = 1
        const val RANOBELIB_SOURCE = 2

        const val SELECTED_SOURCE = 0
    }

    private val readmanga = ReadMangaParser()

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