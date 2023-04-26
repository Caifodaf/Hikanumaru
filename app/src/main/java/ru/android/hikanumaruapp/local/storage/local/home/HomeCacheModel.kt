package ru.android.hikanumaruapp.local.storage.local.home

import ru.android.hikanumaruapp.model.GenresMainModel
import ru.android.hikanumaruapp.model.MangaList

data class HomeCacheModel(
    val genresList:List<GenresMainModel>,
    val mangaList:List<MangaList>,
    val manhvaList:List<MangaList>,
    val manhuaList:List<MangaList>,
    val popularList:List<MangaList>,
)