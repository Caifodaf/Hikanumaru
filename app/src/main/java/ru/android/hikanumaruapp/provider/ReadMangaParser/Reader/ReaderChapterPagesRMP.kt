package ru.android.hikanumaruapp.provider.ReadMangaParser.Reader

import android.util.Log
import org.jsoup.nodes.Document
import org.jsoup.select.Elements
import ru.android.hikanumaruapp.presentasion.reader.model.ReaderChapter
import ru.android.hikanumaruapp.presentasion.reader.model.ReaderChapterPage
import java.io.IOException

class ReaderChapterPagesRMP(
    private val document: Document,
    private val url:String,
    private val chapter: ReaderChapter){

    fun get() : MutableList<ReaderChapterPage> {
        try {
            val element: Elements = document!!.select("script")
            var s: String = element.html();
            var start: Int = 0
            var numberImage: Int = 0
            val pages: MutableList<ReaderChapterPage> = mutableListOf()

            //Pages
            start = s.indexOf("rm_h.initReader( [")
            if (start != -1) {
                start += 10
                val p: Int = s.lastIndexOf("]") + 1
                s = s.substring(start, p)
            }
            s = s.replace("','',\"","")
            s = s.substringAfter('[')

            var temp:String = ""
            val dwx:Boolean = true
            s = s.substringAfter("rm_h.initReader( [2,3], [")
            while(dwx) {

                try {
                    temp = s.substring(s.indexOf('\'') + 1, s.indexOf('\"'))
                }catch (e:RuntimeException){
                    break
                }

                if (temp.indexOf("],[") != -1){ break} //stop

                pages.add(ReaderChapterPage(
                    id = chapter.idChapter,
                    numberImageChapter = numberImage,
                    linkImage = temp,
                ))
                numberImage++
                s = s.replace("\'$temp\"","")
            }

            //if(chapter.linkPagePrev == "Is start page")
            //    pages[0] = TransItem(
            //        type = -1,
            //        curr = chapter.title,
            //        second = "Is start page"
            //    )
            //else{
            //    pages[0] = TransItem(
            //        type = 0,
            //        curr = chapter.title,
            //        second = chapter.pagePrevTitle
            //    )
            //}

            /* selectChapterType
            1- next
            2- translate
            3- Finish */

            //when (chapter.selectType){
            //    1 ->
            //        pages.add(TransItem(
            //            type = 1,
            //            curr = chapter.title,
            //            second = chapter.pageNextTitle
            //        ))
            //    2 ->
            //        pages.add(TransItem(
            //            type = 2,
            //            curr = chapter.title,
            //            second = "Translate"
            //        ))
            //    3 ->
            //        pages.add(TransItem(
            //        type = 3,
            //        curr = chapter.title,
            //        second = "finish"
            //        ))
            //}

            Log.d("dadadadwa", "loadpages pages - $pages")
            return pages
        } catch (e: IOException) {
            Log.e("Error", "Error RCV Download News items ")
            e.printStackTrace();
            return mutableListOf()
        }
    }
}