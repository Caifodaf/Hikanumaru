package ru.android.hikanumaruapp.provider

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.android.hikanumaruapp.data.model.Chapter
import ru.android.hikanumaruapp.data.model.Manga
import ru.android.hikanumaruapp.provider.SourceProvider.MANGALIB_SOURCE
import ru.android.hikanumaruapp.provider.SourceProvider.RANOBELIB_SOURCE
import ru.android.hikanumaruapp.provider.SourceProvider.READMANGA_SOURCE
import ru.android.hikanumaruapp.provider.SourceProvider.SELECTED_SOURCE
import javax.inject.Inject


class Provider @Inject constructor(){

    private val readmanga = ReadMangaParserProvider()
    private var source: String = "readmanga"

    private fun checkSource(){
        //todo add fun
        source = SELECTED_SOURCE
    }

    fun downloadMangaPage(url: String): Flow<Manga> {
        return when (source) {
            //READMANGA_SOURCE -> loadDataMangaPage(url)
            MANGALIB_SOURCE -> flow { }
            RANOBELIB_SOURCE -> flow { }
            else -> flow { }
        }
    }

    fun downloadMangaPageChapters(source: String, sourceLink: String): Flow<MutableList<Chapter>> {

        return when (source) {
            READMANGA_SOURCE -> loadDataMangaPageChapters("https://readmanga.live/$sourceLink")
            MANGALIB_SOURCE -> flow { emit(mutableListOf(Chapter(notChapter = true, null, null, null, null, null, null, null, null, null, null))) }
            RANOBELIB_SOURCE -> flow { emit(mutableListOf(Chapter(notChapter = true, null, null, null, null, null, null, null, null, null, null))) }
            else -> flow { emit(mutableListOf(Chapter(notChapter = true, null, null, null, null, null, null, null, null, null, null))) }
        }
    }

    //private fun loadDataMangaPage(url: String): Flow<Manga> =
        //readmanga.getDatMangaPage(url)

    private fun loadDataMangaPageChapters(url: String): Flow<MutableList<Chapter>> =
        readmanga.getDatMangaChapters(url)

}