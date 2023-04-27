package ru.android.hikanumaruapp.data.model

data class Chapter(
    val notChapter:Boolean = false,

    val id: String? = "0/0", // id - tom/num chapter 4/19
    val numberList:Int?  = 0, // Num in list chapter
    val url:String? = "",

    val tom: String? = "",
    val num: String? = "",
    val description: String?,
    val datePublished: String? = "",
    val translater: String? = "",

    val stateRead: Boolean? = false, // Read true|false
    val stateDownload: Boolean? = false, // Download true|false
)