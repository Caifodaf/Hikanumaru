package ru.android.hikanumaruapp.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.squareup.moshi.Json
import ru.android.hikanumaruapp.utilits.room.ConvertersRoom

//data class MangaInfo (
//    var scoreCount: String = "",
//    var viewCount: String = "",
//    var likesCount: String = "",
//    var commentCount: String = "",
//
//    var author: MutableList<String>,
//    var art: MutableList<String>?,
//    var publisher: MutableList<String>?     ,
//    var dateRelise: String = "",
//    var translators: MutableList<String>?,
//)

@Entity
@TypeConverters
data class Manga (
    @PrimaryKey
    @Json(name = "id")var id: String = "", // id Manga
    @Json(name = "title")var title: String = "",
    @Json(name = "additionalTitle")var additionalTitle: String? = null,
    @field:TypeConverters(ConvertersRoom::class)
    @Json(name = "othersTitle")var othersTitle: List<String>? = null,
    @field:TypeConverters(ConvertersRoom::class)
    @Json(name = "coverLinks")var coverLinks: List<String>? = null,
    @Json(name = "description")var description: String? = null,
    @Json(name = "releaseYear")var releaseYear: Int? = null,
    @Json(name = "ageRating")var ageRating: Int? = null,
    @Json(name = "type")var type: String? = null,
    @Json(name = "publicationStatus")var publicationStatus: String? = null,
    @Json(name = "translationStatus")var translationStatus: String? = null,
    @field:TypeConverters(ConvertersRoom::class)
    @Json(name = "genres")var genres: List<GenresMainModel>? = null,
    @Json(name = "sourceLink")var sourceLink: String = "",
    @Json(name = "source")var source: String = "",
    @Json(name = "createTime")var createTime: String = "",
    @Json(name = "updateTime")var updateTime: String = "",

    var userStatus: String? = null,

    var lastReadChapter: String? = "",
    @field:TypeConverters(ConvertersRoom::class)
    var chapters: MutableList<Chapter>? // List Pages
)

@Entity
@TypeConverters
data class MangaList (
    @PrimaryKey
    @Json(name = "id")var id: String = "", // id Manga
    @Json(name = "title")var title: String = "",
    @field:TypeConverters(ConvertersRoom::class)
    @Json(name = "coverLinks")var coverLinks: MangaListCoverLinks? = null,
    @Json(name = "description")var description: String? = null,
    @Json(name = "releaseYear")var releaseYear: Int? = null,
    @Json(name = "type")var type: String? = null,
    @Json(name = "sourceLink")var sourceLink: String = "",
    @field:TypeConverters(ConvertersRoom::class)
    @Json(name = "mainGenres")var genres: List<GenresMainModel>? = null,

    @Json(name = "userStatus")var userStatus: String? = null,
)

data class MangaListCoverLinks(
    @Json(name = "first")var first: String? = null,
    @Json(name = "last")var last: String? = null,
)
