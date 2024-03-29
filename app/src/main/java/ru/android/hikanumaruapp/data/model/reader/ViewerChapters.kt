package ru.android.hikanumaruapp.data.model.reader

//data class ViewerChapters(
//    var currChapter: MutableList<ReaderChapter>,
//    val prevChapter: MutableList<ReaderChapter>?,
//    val nextChapter: MutableList<ReaderChapter>?,
//)

data class ReaderChapter(
    /* selectChapterType
    1- next
    2- translate
    3- Finish
     */

    var idChapter: String = "0/0", // id num in Manga
    var url: String = "",
    var selectType: Int? = 2, // Type transition
    var tom: String? = "", // Tom chapter - 123
    var number: String? = "", // Number chapter - 123
    var title: String? = "", // Title chapter - "Hello world"
    var description: String? = "", // Description
    var linkPageManga: String = "null", // Link page manga
    var linkPagePrev: String? = "null", // Link page manga
    var pagePrevId:String? = "null",
    var pagePrevTitle:String? = "null",
    var linkPageNext: String? = "null", // Link page manga
    var pageNextId:String? = "null",
    var pageNextTitle:String? = "null",
    val translater: String = "",
    var pages: MutableList<ReaderChapterPage>? = null // List image
)



data class ReaderChapterPage(
    var id: String = "", // tom|nun cur
    var numberImageChapter: Int, // number in chapter
        //var linkPagePrev: String? = "null", // Link page manga
        //var linkPageNext: String? = "null", // Link page manga
    var linkImage: String = "",
    )

data class TransItem(
    /*
    -1 - Start
    0 - Prev
    1 - next
    2 - translate
    3 - Finish
     */
    var type: Int,
    val curr: String? = "",
    val second: String? = ""
)
