package ru.android.hikanumaruapp.provider.ReadMangaParser.Reader

import android.util.Log
import kotlinx.coroutines.flow.flow
import org.jsoup.nodes.Document
import ru.android.hikanumaruapp.model.Chapter
import ru.android.hikanumaruapp.ui.reader.model.ReaderChapterPage
import java.io.IOException

class ReaderChaptersListRMP(
    private val document: Document,
    val url: String) {

    fun get(): MutableList<Chapter> {
        try {
            val pages = mutableListOf<Chapter>()

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


                pages.add(
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

            return pages
        } catch (e: IOException) {
            Log.e("Error", "Error RCV Download News items ")
            e.printStackTrace();
            return mutableListOf<Chapter>()
        }
    }
}
