package ru.android.hikanumaruapp.model

data class MangaInfo (
    var scoreCount: String = "",
    var viewCount: String = "",
    var likesCount: String = "",
    var commentCount: String = "",

    var author: MutableList<String>,
    var art: MutableList<String>?,
    var publisher: MutableList<String>?     ,
    var dateRelise: String = "",
    var translators: MutableList<String>?,
)

data class Manga (
    var id: Int? = 0, // id Manga
    var type: Int? = 0, // Type 0-Manga 1-Manhva 2-Manhuya 3-Web 4-Comick 5-ru
    var name: String = "",
    var alternativeName: String? = "",
    var description: String? = "",
    var score: String? = "0.0",
    var status: Int = 1, // State published manga 0-non 1-translate 2- stoped 3 - finish
    var statusTranslated: Int? = 1,
    var oldRating:String? = "",
    var year: String? = "",

    var image: String? = "",
    var imageBack: String? = "",

    var bookmark: Boolean = false,
    var bookmarkName: Int = 0,
    var lastRead: String? = "",
    var chapterCount: Int? = 0,

    var chapterCountLoadLocal: Int? = 0,
    var chapterCountViewLocal: Int? = 0,

    val info: MutableList<MangaInfo>, // List Info
    var chapter: MutableList<Chapter>? // List Pages


)