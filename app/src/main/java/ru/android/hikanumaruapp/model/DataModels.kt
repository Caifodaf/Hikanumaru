package ru.android.hikanumaruapp.model

import ru.android.hikanumaruapp.presentasion.reader.model.ReaderChapter
import ru.android.hikanumaruapp.presentasion.reader.model.ReaderChapterPage

data class ResultData(
    val error: Boolean,
    val list: MutableList<ReaderChapterPage>
)

data class ResultDataFirst(
    val error: Boolean,
    val list: MutableList<ReaderChapter>?
)

data class TokenJWTModel(
    var token: String?,
    var message: String?,
    var code: Int?,
    )
