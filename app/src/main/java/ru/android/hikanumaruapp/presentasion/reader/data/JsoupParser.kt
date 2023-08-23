package ru.android.hikanumaruapp.presentasion.reader.data

import org.jsoup.Jsoup
import org.jsoup.nodes.Document

internal class JsoupParser {

    private val userAgent: String =
        "Mozilla/5.0 (Windows NT 10.0; Win64; x64) " +
                "AppleWebKit/537.36 (KHTML, like Gecko) " +
                "Chrome/81.0.4044.138 Safari/537.36"

    fun downloadDocument(url: String?): Document? {
        return try {
            Jsoup.connect(url)
                .userAgent(userAgent)
                .header("Content-Encoding", "gzip")
                .header("Content-Language", "ru")
                .ignoreHttpErrors(true)
                .followRedirects(true)
                .referrer("http://www.google.com")
                .timeout(10000)
                .ignoreContentType(true)
                .get()
        }catch (e:Exception){
            return null
        }
    }


}