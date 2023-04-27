package ru.android.hikanumaruapp.data.model

import com.squareup.moshi.Json

data class RecMainModel(
    var title: String = "",
    var description: String = "",
    var linkImageView: String? = ""
)

data class MangaMainModel(
    var title: String = "",
    var type: String = "",
    var state: String = "",
    var linkPage: String = "",
    var linkImageView: String? = ""
)


data class BodyGenresApiModel(
    @Json(name = "page")var page: Int = 1,
)

data class GenresMainModel(
    @Json(name = "id")var id: String = "",
    @Json(name = "title")var title: String = "",
    @Json(name = "description")var description: String? = null,
    @Json(name = "source")var source: String = "",
)

data class JournalMainModel(
    var title: String = "",
    var typeColor: String = "",
    var linkPage: String = "",
)

data class MangaPopularMainModel(
    var title: String = "",
    var author: String = "",
    var type: String = "",
    var state: String = "",
    var score: Double? = 0.0,
    var description: String?,
    var linkPage: String = "",
    var linkImageView: String? = ""
)

//data class ReaderChaptersModel(
//    var idChapter: Int = 0,
//    var selectChapterType: Int = 1,
//    var numberChapter: String? = "",
//    var titleChapter: String? = "",
//    var description: String? = "",
//    var linkPage: String = "null",
//    val pages: MutableList<ReaderChapterLinks>
//)
//
//data class ReaderChapterLinks(
//    var numberImageChapter: Int,
//    var linkImage: String? = "",
//)
