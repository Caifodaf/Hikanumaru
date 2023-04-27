package ru.android.hikanumaruapp.provider.ReadMangaParser.Reader

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.jsoup.nodes.Document
import ru.android.hikanumaruapp.data.model.Chapter
import ru.android.hikanumaruapp.presentasion.reader.model.ReaderChapter

class ReaderRMP
{
    fun getChapter(document: Document, url:String): Flow<ReaderChapter> {
        val chapter = ReaderChapterRMP(document,url).get()
        val pages = ReaderChapterPagesRMP(document,url,chapter).get()
        chapter.pages = pages

        return flow { emit(chapter) }
    }

    fun getChaptersList(document: Document, url: String): Flow<MutableList<Chapter>> {
        return flow { emit(ReaderChaptersListRMP(document,url).get()) }
    }


}