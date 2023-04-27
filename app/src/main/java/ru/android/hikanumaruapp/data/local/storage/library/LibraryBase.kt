package ru.android.hikanumaruapp.data.local.storage.library

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ru.android.hikanumaruapp.data.model.Manga
import ru.android.hikanumaruapp.utilits.room.ConvertersRoom


@Database(entities = [Manga::class], version = 2)
@TypeConverters(ConvertersRoom::class)
abstract class LibraryBase : RoomDatabase() {
    abstract fun LibraryDao(): LibraryDao
}