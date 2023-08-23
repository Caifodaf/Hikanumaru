package ru.android.hikanumaruapp.presentasion.reader.data

import android.util.Log
import kotlinx.coroutines.flow.flow
import org.jsoup.nodes.Document
import ru.android.hikanumaruapp.data.model.Chapter
import ru.android.hikanumaruapp.data.model.reader.ReaderChapter
import ru.android.hikanumaruapp.presentasion.reader.data.UriSourcesParser.READMANGA_URL
import java.io.IOException

class CreativeWorkChaptersListRMP(
    private val document: Document,
    private val url:String
    ) {

    private var chapterList: MutableList<Chapter> = mutableListOf()

    private lateinit var element: String

    fun get(): MutableList<Chapter>? {
        try {
            if (document!!.select("h3").eq(1)
                    .text() != "В этой манге еще нет ни одной главы."
            ) {
                val element =
                    document!!.select("table[class=table table-hover]").select("tr")
                val title = document!!.select("span[class=name]").text()

                for (i in 0 until element.size) {

                    var moreNun = element.select("a").eq(i).text()

                    val url = element.select("a").eq(i).attr("href")

                    val num: String = url.substringAfterLast("/")
                    var tom: String = url.substringAfterLast("/vol")
                    tom = tom.replace("/$num", "")
                    if (tom == READMANGA_URL)
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


                    chapterList.add(
                        Chapter(
                            notChapter = false,
                            id = "$tom/$num",
                            numberList = (element.size - 1) - i,
                            url = READMANGA_URL.dropLast(1) + url,
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
            } else {
                chapterList.add(
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
            logOut()
            return chapterList
        } catch (e: IOException) {
            Log.e("Error", "Error RCV Parser Manga Chapter")
            e.printStackTrace();
            return null
        }
    }


    private fun logOut() {
        for (i in 0 until chapterList.size){
            Log.d("dadadadwa", "loadpages notChapter - ${chapterList[i].notChapter}")
            Log.d("dadadadwa", "loadpages id - ${chapterList[i].id}")
            Log.d("dadadadwa", "loadpages numberList - ${chapterList[i].numberList}")
            Log.d("dadadadwa", "loadpages url - ${chapterList[i].url}")
            Log.d("dadadadwa", "loadpages tom - ${chapterList[i].tom}")
            Log.d("dadadadwa", "loadpages num - ${chapterList[i].num}")
            Log.d("dadadadwa", "loadpages description - ${chapterList[i].description}")
            Log.d("dadadadwa", "loadpages datePublished - ${chapterList[i].datePublished}")
            Log.d("dadadadwa", "loadpages translater - ${chapterList[i].translater}")
            Log.d("dadadadwa", "loadpages stateRead - ${chapterList[i].stateRead}")
            Log.d("dadadadwa", "loadpages stateDownload - ${chapterList[i].stateDownload}")
        }
    }






}
