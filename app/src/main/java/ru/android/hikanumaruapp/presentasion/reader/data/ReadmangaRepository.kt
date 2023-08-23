package ru.android.hikanumaruapp.presentasion.reader.data

import android.util.Log
import org.jsoup.nodes.Document
import ru.android.hikanumaruapp.data.model.Chapter
import javax.inject.Inject

class ReadmangaRepository @Inject constructor(){

    fun getChaptersList(url: String) = networkRequestFlow {
        getChaptersListFlow(url, JsoupParser().downloadDocument(url))
    }

    private suspend fun getChaptersListFlow(url: String, document: Document?): MutableList<Chapter>? =
        if (document != null) CreativeWorkChaptersListRMP(document, url).get() else null

    fun getChapterReader(url: String) = networkRequestFlow {
        JsoupParser().downloadDocument(url).let { document ->
            return@networkRequestFlow if (document != null) (ReaderChapterRMP(document, url).get()) else null
        }
    }











}