package ru.android.hikanumaruapp.presentasion.mangapage.adapter.model

interface TagsMangaPageId {
    companion object {
        const val DIVIDE = 0
        const val GENRE_TAG = 1
        const val RATING_TAG = 2
        const val YEAR_TAG = 3
        const val TYPE_WORK_TAG = 4
        const val AGE_RATING_TAG = 5
        const val PUBLISH_STATUS_TAG = 6
    }
}

data class TagsMangaPageModel (
    var type: Int = TagsMangaPageId.DIVIDE,
    var id: String? = null,
    var text: String? = null,
    var image: String? = null,
    )