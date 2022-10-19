package ru.android.hikanumaruapp.model

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

data class GenresMainModel(
    var title: String = "",
    var typeColor: String = "",
    var linkPage: String = "",
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
