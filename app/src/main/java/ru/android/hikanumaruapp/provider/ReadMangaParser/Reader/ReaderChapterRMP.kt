package ru.android.hikanumaruapp.provider.ReadMangaParser.Reader

import android.util.Log
import org.jsoup.nodes.Document
import org.jsoup.select.Elements
import ru.android.hikanumaruapp.presentasion.reader.model.ReaderChapter
import java.io.IOException

class ReaderChapterRMP(val document: Document, val url:String){

    fun get() : ReaderChapter{
        try {
            val element: Elements = document!!.select("script")
            val s: String = element.html();
            var start: Int = 0
            var nextLink = ""
            var prevLink: String = ""
            var prevId: String = ""
            var prevTitle: String = ""
            var nextId: String = ""
            var nextTitle: String = ""
            var tran: Int = 1
            val num: String = url.substringAfterLast("/")
            var tom: String = url.substringAfterLast("/vol")
            tom = tom.substringBefore("/$num")
            if (tom == "https:readmanga.io/")
                tom = "1"

            //https://readmanga.io/nachalo_posle_konca/vol4/12
            val mangaUrl: String = document!!.select("h1").select("a").attr("href")
            //var nameUrl:String = ""
            //nameUrl = urlU.substringAfter("https:readmanga.io/")
            //nameUrl = nameUrl.substringBefore("/")
            //val mangaUrl: String = "https:readmanga.io/$nameUrl"

            val title = document!!.select("h1").select("a").text()
            val description = (document!!.select("h1").text()).substringAfterLast(num)

            start = s.indexOf("nextChapterLink = \"")
            if (start != -1) {
                val p: Int = s.lastIndexOf("\";") + 1
                nextLink = s.substring(start)
                nextLink = nextLink.substringBefore("\";")
                nextLink = nextLink.substringAfter("\"/")

                nextId = "${nextLink.substringAfterLast("/vol").substringBefore("/")} " +
                        nextLink.substringAfterLast("/").substringBefore("\"")
                val docNext = document!!.select("select[id=chapterSelectorSelect]").toString()
                var nextTemp = docNext.substringAfter("$nextLink\">")
                nextTemp = nextTemp.substringBefore("</option>")

                nextTitle = nextTemp.substringAfter("${nextLink.substringAfterLast("/vol").substringBefore("/")} " +
                        "- ${nextLink.substringAfterLast("/").substringBefore("\"")} ")

                nextLink = "https://readmanga.io/$nextLink"
                tran = when (nextLink == "finish") {
                    true -> 2
                    false -> 1
                }
            }

            prevLink = "https://readmanga.io${
                document!!.select("a[class=hide prevChapLink]")
                    .attr("href")}"

            if(prevLink == "https://readmanga.io$mangaUrl") {
                prevLink = "Is start page"
                prevId = "-/-"
                prevTitle = "Is start page"
            }else{
                val prevTempAll:String = "<option value=\"${document!!.select("a[class=hide prevChapLink]")
                    .attr("href")}\">"

                prevId = prevTempAll.substringAfterLast("/vol").substringBefore("/") + " " +
                        prevTempAll.substringAfterLast("/").substringBefore("\"")

                val docPrev = document!!.select("select[id=chapterSelectorSelect]").toString()
                var prevTemp = docPrev.substringAfterLast(prevTempAll)

                prevTemp = prevTemp.substringBefore("</option>")
                prevTitle = prevTemp.substringAfter("${prevTempAll.substringAfterLast("/vol").substringBefore("/")}" +
                        " - ${prevTempAll.substringAfterLast("/").substringBefore("\"")} ")

            }



            val translator = document!!.select("h5").select("a").text()

            val chapter = ReaderChapter(
                idChapter = "$tom/$num",
                url = url,
                selectType = tran,
                tom = tom,
                number = num,
                title = title,
                description = description,
                linkPageManga = mangaUrl,
                linkPagePrev = prevLink,
                pagePrevId = prevId,
                pagePrevTitle = prevTitle,
                linkPageNext = nextLink,
                pageNextId = nextId,
                pageNextTitle = nextTitle,
                translater = translator,
            )
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

            return chapter
        }catch (e: IOException) {
            Log.e("Error", "Error RCV Download News items ")
            e.printStackTrace();
            return  ReaderChapter()
        }
    }
}