package ru.android.hikanumaruapp.presentasion.reader.data

import android.util.Log
import org.jsoup.nodes.Document
import org.jsoup.select.Elements
import ru.android.hikanumaruapp.data.model.reader.ReaderChapter
import ru.android.hikanumaruapp.data.model.reader.ReaderChapterPage
import ru.android.hikanumaruapp.presentasion.reader.data.UriSourcesParser.READMANGA_URL
import java.io.IOException

class ReaderChapterRMP(
    private var document: Document,
    private val url:String
    ) {

    private lateinit var chapter: ReaderChapter

    private lateinit var element: String

    private lateinit var idChapter: String
    private lateinit var tomChapter: String
    private lateinit var numberChapter: String
    private lateinit var titleChapter: String
    private var descriptionChapter: String? = null
    private lateinit var translatorChapter: String

    private lateinit var linkPageMangaChapter: String

    private var nextLinkChapter: String? = null
    private var nextIdChapter: String? = null
    private var nextTitleChapter: String? = null

    private var prevLinkChapter: String? = null
    private var prevIdChapter: String? = null
    private var prevTitleChapter: String? = null

    private var selectType: Int = 0

    fun get(): ReaderChapter? {
        try {
            //https://readmanga.live/zvezdnoe_ditia__A533b
            //https://readmanga.io/nachalo_posle_konca/vol4/12
            //https://readmanga.live/moia_liubov_999_urovnia_k_iamada_kunu__A5274/vol7/65
            element = document.select("script").html()

            chapterInfo(document) { id, tom, number, title, description, translator ->
                idChapter = id
                tomChapter = tom
                numberChapter = number
                titleChapter = title
                if (description != null)
                    descriptionChapter = description
                translatorChapter = translator
            }

            linkPageMangaChapter = linkPageManga(document)

            prevChapter(document) { prevLink, prevId, prevTitle ->
                prevLinkChapter = prevLink
                prevIdChapter = prevId
                prevTitleChapter = prevTitle
            }

            nextChapter(document) { nextLink, nextId, nextTitle, type ->
                nextLinkChapter = nextLink
                nextIdChapter = nextId
                nextTitleChapter = nextTitle

                selectType = type
            }

            val pages = getPages()

            chapter = ReaderChapter(
                idChapter = idChapter,
                url = url,
                selectType = selectType,
                tom = tomChapter,
                number = numberChapter,
                title = titleChapter,
                description = descriptionChapter,
                linkPageManga = linkPageMangaChapter,
                linkPagePrev = prevLinkChapter,
                pagePrevId = prevIdChapter,
                pagePrevTitle = prevTitleChapter,
                linkPageNext = nextLinkChapter,
                pageNextId = nextIdChapter,
                pageNextTitle = nextTitleChapter,
                translater = translatorChapter,
                pages = pages
            )
            logOut()
            Log.e("ErrorNetwork", "Reader getDataChapter 5 ")
            return chapter
        } catch (e: IOException) {
            Log.e("Error", "Error RCV Parser Manga Chapter")
            e.printStackTrace();
            return null
        }
    }

    //['https://one-way.work/','',"auto/66/10/49/02.png_res.jpg?t=1683319193&u=0&h=AhtEZkYwqAKfkGcyA-rUJg",1472,2048]
    //view-source:https://readmanga.live/zvezdnoe_ditia__A533b/vol1/1?mtr=1
    private fun getPages(): MutableList<ReaderChapterPage>? {
        try {
            val pages = mutableListOf<ReaderChapterPage>()
            var elementT = document.select("script").html()
            if (elementT.indexOf("rm_h.readerInit( 0,[[") == -1)
                reparseDocument().let { newDoc ->
                    if (newDoc != null) document = newDoc else return null
                    elementT = document.select("script").html()
                }
            // Get the image links
            val start = elementT.indexOf("rm_h.readerInit( 0,[[") + 21
            val end = elementT.lastIndexOf("]]") + 1
            Log.e("ErrorNetwork", "Reader getDataChapter 5  ${url}")
            Log.e("ErrorNetwork", "Reader getDataChapter 5  ${elementT.indexOf("rm_h.readerInit( 0,[[")}")

            val imageLinks = elementT.substring(start, end)
                .replace("','',\"", "")
                .split(",")

            Log.e("ErrorNetwork", "Reader getDataChapter 11")
            // Add each image link to the list of pages
            var numberPage = 0
            imageLinks.forEachIndexed { index, linkT ->
                val link = linkT.drop(2)
                if (link.contains("https://")) {
                    pages.add(ReaderChapterPage(
                        id = idChapter,
                        numberImageChapter = numberPage,
                        linkImage = link.substringBefore("?")
                    ))
                    numberPage++
                }
            }

            return pages
        }catch (e:Exception){
            return null
        }
    }

    private fun reparseDocument() =
        JsoupParser().downloadDocument("$url?mtr=1")


    private fun chapterInfo(
        document: Document,
        param: (id: String, tom: String, number: String, title: String, description: String?, translator: String) -> Unit
    ) {
        val number = url.substringAfterLast("/")
        var tom = url.substringAfterLast("/vol").substringBefore("/$number")
        if (tom == READMANGA_URL) tom = "1"


        val title = document.select("h1 a").text()
        val description = document.select("h1").text().substringAfterLast(number)
            .takeIf { it.isNotEmpty() }
        val translator = document.select("h5 a").text()

        param("$tom/$number", tom, number, title, description, translator)
    }

    private fun linkPageManga(document: Document) =
        document.select("h1 a").attr("href")

    private fun nextChapter(
        document: Document,
        param: (nextLink: String?, nextId: String?, nextTitle: String?, type: Int) -> Unit
    ) {
        val nextLinkRegex = "nextLink = true".toRegex()
        if (nextLinkRegex.containsMatchIn(document.toString())) {
            var nextId = "finish"
            var nextTitle = "finish"

            // var nextChapterLink = "/moia_liubov_999_urovnia_k_iamada_kunu/finish";
            val link = element.substringAfter("nextChapterLink = \"")
                .substringBefore('"')
            // /moia_liubov_999_urovnia_k_iamada_kunu/finish || /moia_liubov_999_urovnia_k_iamada_kunu__A5274/vol7/68

            val type = if (link.endsWith("finish")) 2 else 1 // .../vol7/68 || finish

            if (type == 1) {
                val number = link.substringAfterLast('/') // 68
                val tom = link.substringBeforeLast("/$number") // .../vol7
                    .substringAfterLast("/vol")  // 7
                nextId = "$tom/$number"
                nextTitle = document.select("a[href=\"$link\"]")
                    .select("a[class=\"chapter-link cp-l\"]") // 1 - 5 Настало время для босса
                    .text()
                    .substringAfter("$tom - $number ")
            }

            param(READMANGA_URL.dropLast(1) + link, nextId, nextTitle, type)
        } else {  //nextLink = false
            param(READMANGA_URL + "finish", "finish", "finish", 2)
        }
    }

    private fun prevChapter(
        document: Document,
        param: (prevLink: String?, prevId: String?, prevTitle: String?) -> Unit
    ) {
        val prevLinkRegex = "prevLink = true".toRegex()
        if (prevLinkRegex.containsMatchIn(document.toString())) {
            // var prevChapterLink = "/moia_liubov_999_urovnia_k_iamada_kunu/vol1/2"
            val link = element.substringAfter("prevChapterLink = \"")
                .substringBefore('"')
            // /moia_liubov_999_urovnia_k_iamada_kunu__A5274/vol7/68

            val number = link.substringAfterLast('/') // 68
            val tom = link.substringBeforeLast("/$number") // .../vol7
                .substringAfterLast("/vol") // 7
            val prevId = "$tom/$number"

            val prevTitle = document.select("a[href=\"$link\"]")
                .select("a[class=\"chapter-link cp-l\"]") // 1 - 5 Настало время для босса
                .text()
                .substringAfter("$tom - $number ")
            return param(READMANGA_URL.dropLast(1) + link, prevId, prevTitle)
        } else { // prevLink = false
            return param("Is start page", "-/-", "Is start page")
        }
    }


    private fun logOut() {
        Log.d("dadadadwa", "loadpages idChapter - ${chapter.idChapter}")
        Log.d("dadadadwa", "loadpages url - ${chapter.url}")
        Log.d("dadadadwa", "loadpages selectType - ${chapter.selectType}")
        Log.d("dadadadwa", "loadpages tom - ${chapter.tom}")
        Log.d("dadadadwa", "loadpages number - ${chapter.number}")
        Log.d("dadadadwa", "loadpages title - ${chapter.title}")
        Log.d("dadadadwa", "loadpages description - ${chapter.description}")
        Log.d("dadadadwa", "loadpages linkPageManga - ${chapter.linkPageManga}")
        Log.d("dadadadwa", "loadpages linkPagePrev - ${chapter.linkPagePrev}")
        Log.d("dadadadwa", "loadpages pagePrevId - ${chapter.pagePrevId}")
        Log.d("dadadadwa", "loadpages pagePrevTitle - ${chapter.pagePrevTitle}")
        Log.d("dadadadwa", "loadpages linkPageNext - ${chapter.linkPageNext}")
        Log.d("dadadadwa", "loadpages pageNextId - ${chapter.pageNextId}")
        Log.d("dadadadwa", "loadpages pageNextTitle - ${chapter.pageNextTitle}")
        Log.d("dadadadwa", "loadpages translater - ${chapter.translater}")
        Log.d("dadadadwa", "loadpages pages - ${chapter.pages}")
    }


}

