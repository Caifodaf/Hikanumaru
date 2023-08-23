package ru.android.hikanumaruapp.presentasion.reader.data

import junit.framework.TestCase
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import java.io.File

class ReaderChapterRMPTest : TestCase() {
    private lateinit var document: Document

    @Before
    override fun setUp() {
        val file = File(javaClass.classLoader.getResource("test_page.html").toURI())
        document = Jsoup.parse(file, "UTF-8")
    }

    @Test
    fun testGet() {
        val url = "https://readmanga.live/naruto/vol71/700"
        val readerChapterRMP = ReaderChapterRMP(document, url)
        val chapter = readerChapterRMP.get()

        assertNotNull(chapter)
        assertEquals("71/700", chapter!!.idChapter)
        assertEquals(url, chapter.url)
        assertEquals(1, chapter.selectType)
        assertEquals("71", chapter.tom)
        assertEquals("700", chapter.number)
        assertEquals("Naruto - 700", chapter.title)
        assertEquals("The Seal of Reconciliation", chapter.description)
        assertEquals("/naruto/700", chapter.linkPageManga)
        assertEquals("https://readmanga.live/naruto/vol71/699", chapter.linkPagePrev)
        assertEquals("71/699", chapter.pagePrevId)
        assertEquals("The Seal of Confrontation", chapter.pagePrevTitle)
        assertEquals("https://readmanga.live/naruto/vol71/701", chapter.linkPageNext)
        assertEquals("71/701", chapter.pageNextId)
        assertEquals("Naruto - 701", chapter.pageNextTitle)
        assertEquals("By Naruto Project", chapter.translater)
    }
}