package ru.android.hikanumaruapp.ui.profile.folders

interface FolderListDefaultObject {

    fun setListDefaultTags() : MutableList<FolderLibraryModel>{
        val list = mutableListOf<FolderLibraryModel>()
        list.add(
            FolderLibraryModel(
                idName = "All",
                countTitle = 0,
                select = true,
            ))
        list.add(
            FolderLibraryModel(
                idName = "Read",
                countTitle = 0,
                select = true,
            ))
        list.add(
            FolderLibraryModel(
                idName = "Planed",
                countTitle = 0,
                select = true,
            ))
        list.add(
            FolderLibraryModel(
                idName = "Abandoned",
                countTitle = 0,
                select = true,
            ))
        list.add(
            FolderLibraryModel(
                idName = "Stopped",
                countTitle = 0,
                select = true,
            ))

        return list
    }
}