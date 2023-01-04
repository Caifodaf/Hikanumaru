package ru.android.hikanumaruapp.provider

import android.util.Log
import android.util.Patterns
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements
import ru.android.hikanumaruapp.model.Chapter
import ru.android.hikanumaruapp.model.Manga
import ru.android.hikanumaruapp.model.MangaInfo
import ru.android.hikanumaruapp.provider.ReadMangaParser.Reader.ReaderRMP
import ru.android.hikanumaruapp.ui.reader.model.ReaderChapter
import ru.android.hikanumaruapp.ui.reader.model.ReaderChapterPage
import java.io.IOException

data class MangaPageInfoModel(
    var type: String = "",
    var author: String = "",
    var artist: String = "",
    var publisher: String = "",
    var statusTranslate: String = "",
    var statusManga: String = "",
    var vivers: String = "",
    var dateRelised: String = "",
    //var genres: Array<String>?
)

class ReadMangaParserProvider {
    private val userAgent: String = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) " +
            "AppleWebKit/537.36 (KHTML, like Gecko) " +
            "Chrome/81.0.4044.138 Safari/537.36";
    private var document: Document? = null

    private fun uploadDocumentPage(url: String?):Boolean {
        return try {
            if (url!=null&&url!="null"){
                if (document == null) {
                    downloadDocument(url) }
                else if (document!!.select("meta[itemprop=url]")!!.attr("content").toString() != url) {
                    downloadDocument(url) }
                true
            }else{
                false
            }
        }catch (e: IOException) {
            e.printStackTrace();
            false
        }
    }

    fun getDatMangaInfo(url: String, listPage: MutableList<MangaPageInfoModel>): MutableList<MangaPageInfoModel>? {
        if (Patterns.WEB_URL.matcher(url).matches()) {
            try {
                val document: Document? = Jsoup.connect(url).get()
                //print(document)
                val element =
                    document!!.select("article[class=media-container]")

                    val type: String = element.select("div[class=media-name__main]")
                        //.select("a")
                        //.eq(i)
                        .text()
                    val author: String = element.select("div[class=media-name__main]")
                        //.select("a")
                        //.eq(i)
                        .text()
                    val artist: String = element.select("div[class=media-name__main]")
                        .select("a")
                        //.eq(i)
                        .text()
                    val publisher: String = element.select("div[class=media-name__main]")
                        //.select("a")
                        //.eq(i)
                        .text()
                    val statusTranslate: String = element.select("div[class=media-name__main]")
                        //.select("a")
                        //.eq(i)
                        .text()
                    val statusManga: String = element.select("div[class=media-name__main]")
                        //.select("a")
                        //.eq(i)
                        .text()
                    val vivers: String = element.select("div[class=media-name__main]")
                        //.select("a")
                        //.eq(i)
                        .text()
                    val dateRelised: String = element.select("div[class=media-name__main]")
                        //.select("a")
                        //.eq(i)
                        .text()
//                    val genresSize: Int = element.select("div[class=media-tags]")
//                        .select("a")
//                        .size
//                        .toInt()
//                for (i in 0 until genresSize){
//                        val genres: String = element.select("div[class=media-name__main]")
//                            //.select("a")
//                            .eq(i)
//                            .text()
//                    }
                    listPage.add(
                        MangaPageInfoModel(
                            type,
                            author,
                            artist,
                            publisher,
                            statusTranslate,
                            statusManga,
                            vivers,
                            dateRelised
                        )
                    )
                return listPage
            } catch (e: IOException) {
                // TODO: Debug out info
                Log.d("Error", "Error RCV Download News items")
                return null
            }
        }
        return null
    }

    fun getDatMangaPage(url: String?, listPage: MutableList<Manga>): Flow<MutableList<Manga>> {
        Log.d("daknxck", "par1 $url - $document")
        if (uploadDocumentPage(url)) {
            if (document != null) {
                val list: MutableList<Manga>
                val listChap: MutableList<Chapter> = mutableListOf()
                val listInfo: MutableList<MangaInfo> = mutableListOf()
                val element =
                    document!!.select("table[class=table table-hover]").select("tr")
                val chapterCount: Int = element.size

                val typeR: String = document!!.select("h1[class=names]")
                    .text()
                val type = when (typeR) {
                    //0-Manga 1-Manhva 2-Manhuya 3 -Comick 4-ru
                    "Манга" -> 0
                    "OLED-Манга" -> 0
                    "Манхва" -> 1
                    "Маньхуа" -> 2
                    "Комикс" -> 3
                    "Руманга" -> 4
                    else -> 0
                }
                var title: String = document!!.select("span[class=name]")
                    .text()
                Log.d("dadad", "til - $title")
                title = title.replace("\\s+".toRegex(), " ")
                Log.d("dadad", "til - $title")
                val alternativeName: String = document!!.select("span[class=eng-name]")
                    .text()
                var description: String = document!!.select("div[class=manga-description]")
                    .eq(0)
                    .select("p")
                    .eq(0)
                    .text()
                if (description.isEmpty()) {
                    description = document!!.select("div[class=manga-description]")
                        .eq(0)
                        .select("span")
                        .eq(0)
                        .text()
                }
                if (description.isEmpty()) {
                    description = document!!.select("div[class=manga-description]")
                        .eq(0)
                        .select("div")
                        .eq(0)
                        .text()
                }
                val score: String? =
                    document!!.select("span[class=rating-block]")
                        .attr("data-score")
                        .toString()

                val statusPageD = document!!.select("div[class=subject-meta ]")
                    .select("p")
                    .text()

                var statusPage = 1
                var statusTranslatedPag = 1

                if (statusPageD.indexOf("выпуск продолжается") != -1) {
                    statusPage = 1
                    statusTranslatedPag = 1
                } else {
                    statusPage = 3
                    statusTranslatedPag = 3
                }
//                    val statusTranslatedPageD = document!!.select("div[class=subject-meta ]")
//                        .select("p")
//                        .eq(1)
//                        .text()

                var lodRatingPage = document!!.select("span[class=elem_limitation]")
                    .select("a")
                    .text()
                if (lodRatingPage.isNullOrEmpty())
                    lodRatingPage = "G"

                val yearPage = document!!.select("span[class=elem_year ]")
                    .select("a")
                    .text()

                val linkImageTop: String? =
                    document!!.select("div[class=picture-fotorama]")
                        .select("img")
                        .attr("src")

                // ----------------------CHAPTER-----------------------------

                if (document!!.select("h3").eq(1)
                        .text() != "В этой манге еще нет ни одной главы."
                ) {
                    if (chapterCount <= 8) {
                        Log.d("daknxck", "chap < 8")
                        chapterMangaPageSelect(chapterCount, 0, element, listChap)
                    } else if (chapterCount > 8) {
                        Log.d("daknxck", "chap more")
                        chapterMangaPageSelect(chapterCount, 1, element, listChap)
                    }

                } else {
                    Log.d("daknxck", "chap null")
                    listChap.add(
                        Chapter(
                            notChapter = true,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null
                        )
                    )
                }


                var dateRelise: String = ""
                var last: MutableList<Chapter>

                if (!listChap.isNullOrEmpty()) {
                    if (!listChap[0].notChapter) {
                        last = listChap.takeLast(1).toMutableList()
                        dateRelise = last[0].datePublished.toString()
                    } else
                        dateRelise = yearPage
                }
                // -------------------------INFO---------------------------


                val elementInfo =
                    document!!.select("p[class=elementList]")

                val author: MutableList<String> = mutableListOf()
                var elementsAuthor = elementInfo.select("span[class=elem_author ]")
                if (elementsAuthor.isNullOrEmpty()) {
                    elementsAuthor = elementInfo.select("span[class=elem_screenwriter ]")
                }

                for (i in 0 until elementsAuthor.size) {
                    val addA = elementsAuthor.select("a").text()
                    author.add(i, addA)
                }

                val art: MutableList<String> = mutableListOf()
                val elementsArt = elementInfo.select("span[class=elem_illustrator ]")

                for (i in 0 until elementsArt.size) {
                    val addA = elementsArt.select("a").text()
                    art.add(i, addA)
                }

                val publisher: MutableList<String> = mutableListOf()
                val elementsPublisher = elementInfo.select("span[class=elem_publisher ]")

                for (i in 0 until elementsPublisher.size) {
                    val addA = elementsPublisher.text()
                    publisher.add(i, addA)
                }

                val translators: MutableList<String> = mutableListOf()
                val elementsTranslators = elementInfo.select("span[class=elem_translator ]")

                for (i in 0 until elementsTranslators.size) {
                    val addA = elementsTranslators.text()
                    translators.add(i, addA)
                }


                listInfo.add(
                    MangaInfo(
                        scoreCount = "0",
                        viewCount = "0",
                        likesCount = "0",
                        commentCount = "0",
                        author = author,
                        art = art,
                        publisher = publisher,
                        dateRelise = dateRelise,
                        translators = translators
                    )
                )

                list = mutableListOf(
                    Manga(
                        id = 0,
                        type = type,
                        name = title,
                        alternativeName = alternativeName,
                        description = description,
                        score = score,
                        status = statusPage,
                        statusTranslated = statusTranslatedPag,
                        oldRating = lodRatingPage,
                        year = yearPage,
                        image = linkImageTop,
                        imageBack = linkImageTop,
                        bookmark = false,
                        bookmarkName = 0,
                        lastRead = "first",
                        chapterCount = chapterCount,
                        info = listInfo,
                        chapter = listChap
                    )
                )

                listPage.addAll(list)

                return flow {
                    emit(listPage)
                }
            }
        }
        return flow {
            emit(listPage)
        }
    }

    private fun chapterMangaPageSelect(count:Int,type:Int,element:Elements,list:MutableList<Chapter>):MutableList<Chapter> {
        //More 8  == 1
        //Min 8  == 0

        if (type == 1) {
            val title: String = document!!.select("span[class=name]")
                .text()

            for (i in 0..3) {

                var moreNun = element.select("a").eq(i).text()
//
                val url = "https://readmanga.io/${element.select("a").eq(i).attr("href")}"
//
                val num: String = url.substringAfterLast("/")
                var tom: String = url.substringAfterLast("/vol")
                tom = tom.replace("/$num", "")
                if (tom == "https:readmanga.io/")
                    tom = "1"
//
                moreNun = moreNun.replace(title, "")
                val descD = "$tom - $num"
                var description = moreNun.replace(descD, "")
                if (description.indexOf(descD) != -1) {
                    description = "Описания нет"
                }
//
                val datePublished =
                    element.select("td[align=right]").eq(i).attr("data-date")
//
                var translater = element.select("a").eq(i).attr("title")
                translater = translater.replace(" (Переводчик)", "")
//
                list.add(
                    Chapter(
                        notChapter = false,
                        id = "$tom/$num",
                        numberList = (count-1) - i,
                        url = url,
                        tom = tom,
                        num = num,
                        description = description,
                        datePublished = datePublished,
                        translater = translater,
                        stateRead = false,
                        stateDownload = false,
                    )
                )
            }

            //---------------------------------------------

            //val start:Int = count-1
            //val last:Int = count-5
            //Log.d("daknxck", "chap two - $start - $last")

            var numFor = -1
            val s:Int = count - 2
            val e:Int = count - 6
            for (i in e..s) {
                numFor+=1

                var moreNun = element.select("a").eq(i).text()

                val url = "https://readmanga.io/${element.select("a").eq(i).attr("href")}"

                val num: String = url.substringAfterLast("/")
                var tom: String = url.substringAfterLast("/vol")
                tom = tom.replace("/$num", "")
                if (tom == "https:readmanga.io/")
                    tom = "1"

                moreNun = moreNun.replace(title, "")
                val descD = "$tom - $num"
                var description = moreNun.replace(descD, "")
                if (description.indexOf(descD) != -1) {
                    description = "Описания нет"
                }

                val datePublished =
                    element.select("td[align=right]").eq(i).attr("data-date")

                var translater = element.select("a").eq(i).attr("title")
                translater = translater.replace(" (Переводчик)", "")

                list.add(
                    Chapter(
                        notChapter = false,
                        id = "$tom/$num",
                        numberList = (count-1) - i,
                        url = url,
                        tom = tom,
                        num = num,
                        description = description,
                        datePublished = datePublished,
                        translater = translater,
                        stateRead = false,
                        stateDownload = false,
                    )
                )
            }
        }

        if (type == 0) {
            for (i in 0 until count-1) {

                val title: String = document!!.select("span[class=name]")
                    .text()

                var moreNun = element.select("a").eq(i).text()

                val url = "https://readmanga.io/${element.select("a").eq(i).attr("href")}"

                val num: String = url.substringAfterLast("/")
                var tom: String = url.substringAfterLast("/vol")
                tom = tom.replace("/$num", "")
                if (tom == "https:readmanga.io/")
                    tom = "1"

                moreNun = moreNun.replace(title, "")
                val descD = "$tom - $num"
                var description = moreNun.replace(descD, "")
                if (description.indexOf(descD) != -1) {
                    description = "Описания нет"
                }

                val datePublished =
                    element.select("td[align=right]").eq(i).attr("data-date")

                var translater = element.select("a").eq(i).attr("title")
                translater = translater.replace(" (Переводчик)", "")

                list.add(
                    Chapter(
                        notChapter = false,
                        id = "$tom/$num",
                        numberList = (count-1) - i,
                        url = url,
                        tom = tom,
                        num = num,
                        description = description,
                        datePublished = datePublished,
                        translater = translater,
                        stateRead = false,
                        stateDownload = false,
                    )
                )

                //Log.d("daknxck", "chap <8 - $i - ${list[i-1]}")
            }
        }
        return list
    }

    fun getDatMangaChapters(urlPage: String?, listPage: MutableList<Chapter>):Flow<MutableList<Chapter>>{
        Log.d("daknxck", "chap list all")
        if (uploadDocumentPage(urlPage)) {
            if (document != null) {
                if (document!!.select("h3").eq(1)
                        .text() != "В этой манге еще нет ни одной главы."
                ) {

                    val element =
                        document!!.select("table[class=table table-hover]").select("tr")
                    val title = document!!.select("span[class=name]").text()

                    for (i in 0 until element.size) {

                        var moreNun = element.select("a").eq(i).text()

                        val url =
                            "https://readmanga.io/${element.select("a").eq(i).attr("href")}"

                        val num: String = url.substringAfterLast("/")
                        var tom: String = url.substringAfterLast("/vol")
                        tom = tom.replace("/$num", "")
                        if (tom == "https:readmanga.io/")
                            tom = "1"

                        moreNun = moreNun.replace(title, "")
                        val descD = "$tom - $num"
                        var description = moreNun.replace(descD, "")
                        if (description.indexOf(descD) != -1) {
                            description = "Описания нет"
                        }

                        val datePublished =
                            element.select("td[align=right]").eq(i).attr("data-date")

                        var translater = element.select("a").eq(i).attr("title")
                        translater = translater.replace(" (Переводчик)", "")


                        listPage.add(
                            Chapter(
                                notChapter = false,
                                id = "$tom/$num",
                                numberList = (element.size - 1) - i,
                                url = url,
                                tom = tom,
                                num = num,
                                description = description,
                                datePublished = datePublished,
                                translater = translater,
                                stateRead = false,
                                stateDownload = false,
                            )
                        )
                    }
                    //Log.d("daknxck", "chap list all - ${listPage.reverse()}")
                } else {
                    listPage.add(
                        Chapter(
                            notChapter = true,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null
                        )
                    )
                }

                return flow { emit(listPage) }

            }
        }
        return flow { emit(listPage) }
    }


    fun getDataMangaChapterReader(url:String): Flow<ReaderChapter>  =
        ReaderRMP().getChapter(downloadDocument(url),url)

    fun getDataReaderChapters(url:String): Flow<MutableList<Chapter>>  =
        ReaderRMP().getChaptersList(downloadDocument(url),url)


//    fun getDataMangaChapterReaderPage(url:String,listPage: MutableList<ReaderChapterPage>): MutableList<ReaderChapterPage> {
//        downloadDocument(url)
//        try {
//            val element: Elements = document!!.select("script")
//            var s: String = element.html();
//            var start: Int = 0
//            var nextLink = ""
//            var prevLink: String = ""
//            val pages: MutableList<String> = mutableListOf()
//            val num: String = url.substringAfterLast("/")
//            var tom: String = url.substringAfterLast("/vol")
//            tom = tom.substringBefore("/$num")
//            if (tom == "https:readmanga.io/")
//                tom = "1"
//
//            val mangaUrl: String = document!!.select("h1").select("a").attr("href")
//
//            start = s.indexOf("nextChapterLink = \"")
//            if (start != -1) {
//                val p: Int = s.lastIndexOf("\";") + 1
//                nextLink = s.substring(start)
//                nextLink = nextLink.substringBefore("\";")
//                nextLink = nextLink.substringAfter("\"/")
//                nextLink = "https://readmanga.io/$nextLink"
//            }
//
//            prevLink = "https://readmanga.io${
//                document!!.select("a[class=hide prevChapLink]")
//                    .attr("href")
//            }"
//
//
//            if(prevLink == "https://readmanga.io$mangaUrl")
//                prevLink = "Is start page"
//
//
//            //Pages
//            start = s.indexOf("rm_h.init(")
//            if (start != -1) {
//                start += 10
//                val p: Int = s.lastIndexOf("]") + 1
//                s = s.substring(start, p)
//            }
//            s = s.replace("','',\"", "")
//            s = s.substringAfter('[')
//
//            var temp: String = ""
//            val dwx: Boolean = true
//            while (dwx) {
//                temp = s.substring(s.indexOf('\'') + 1, s.indexOf('\"'))
//                if (temp.indexOf("],[") != -1) {
//                    break
//                }
//                pages.add(temp)
//                s = s.replace("\'$temp\"", "")
//            }
//
//            for (i in 0 until pages.size)
//                listPage.add(ReaderChapterPage(i, "$tom/$num", prevLink, nextLink, pages[i]))
//            Log.d("Adapter1", " items - $listPage")
//
//            return listPage
//        }catch (e: IOException) {
//            Log.e("Error", "Error RCV Download News items ")
//            e.printStackTrace();
//            return listPage
//        }
//    }


    private fun downloadDocument(url: String?):Document {
        document = Jsoup.connect(url)
            .userAgent(userAgent)
            .header("Content-Encoding", "gzip")
            .header("Content-Language", "ru")
            .ignoreHttpErrors(true)
            .followRedirects(true)
            .referrer("http://www.google.com")
            .timeout(10000)
            .ignoreContentType(true)
            .get()
        Log.d("document", "Document reparse")
        return document!!
    }

}