package ru.android.hikanumaruapp.data.local.storage.library

import androidx.room.*
import ru.android.hikanumaruapp.data.model.Manga

@Dao
interface LibraryDao {
    @Insert
    suspend fun insertAll(vararg manga: Manga)

    @Update
    suspend fun update(manga: Manga)

    @Delete
    suspend fun delete(manga: Manga)

    //@Query("DELETE FROM manga")
    //suspend fun clearDataBase()

    @Query("SELECT * FROM manga")
    suspend fun getAllLibrary(): List<Manga>

    @Query("SELECT * FROM manga WHERE id LIKE :id")
    suspend fun getById(id:String): Manga?

//    @Query("SELECT * FROM newsModel WHERE isFavorite LIKE :isFavorite")
//    suspend fun getAllNewsWithFavorite(isFavorite: Boolean): List<NewsModel>
}