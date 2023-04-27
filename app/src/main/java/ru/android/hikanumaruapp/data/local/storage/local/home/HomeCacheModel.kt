package ru.android.hikanumaruapp.data.local.storage.local.home

import ru.android.hikanumaruapp.data.model.GenresMainModel
import ru.android.hikanumaruapp.data.model.MangaList

data class HomeCacheModel(
    val genresList:List<GenresMainModel>,
    val mangaList:List<MangaList>,
    val manhvaList:List<MangaList>,
    //val manhuaList:List<MangaList>,
    //val popularList:List<MangaList>,
)