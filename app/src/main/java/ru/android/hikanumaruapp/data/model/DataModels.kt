package ru.android.hikanumaruapp.data.model

import ru.android.hikanumaruapp.data.model.reader.ReaderChapter
import ru.android.hikanumaruapp.data.model.reader.ReaderChapterPage

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
