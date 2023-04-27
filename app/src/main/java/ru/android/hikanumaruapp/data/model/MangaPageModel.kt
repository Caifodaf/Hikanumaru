package ru.android.hikanumaruapp.data.model

data class MangaPageModel(
    var title: String = "",
    var alternativeTitle: String? = "",
    var description: String = "",
    var score: String? = "0.0",
    var linkImageView: String? = "",
    var linkImageBackManga: String? = ""
)

data class MangaPageInfoModel(
    var title: String? = "",
)

data class MangaPageChapterModel(
    var numberChapter: String? = "",
    var title: String? = "",
    var description: String? = "",
    var checkNew: Boolean? = false,
    var date: String? = "10.2.2222",
    var linkChapter: String? = ""
)

data class MangaPageSimilarModel(
    var title: String = "",
    //var alternativeTitle: String? = "",
    var description: String = "",
    var score: String? = "0.0",
    var linkImageView: String? = "",
    var linkImageBackManga: String? = ""
)

data class MangaPageTextDate(
    var type: Int = 0,
    var status: Int? = 0,
    var statusTranslate: Int? = 0,
    var year: String? = "",
    var author: String? = ""
)