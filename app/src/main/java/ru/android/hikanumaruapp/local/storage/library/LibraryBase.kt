package ru.android.hikanumaruapp.local.storage.library

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ru.android.hikanumaruapp.model.Manga
import ru.android.hikanumaruapp.utilits.room.ConvertersRoom


@Database(entities = [Manga::class], version = 2)
@TypeConverters(ConvertersRoom::class)
abstract class LibraryBase : RoomDatabase() {
    abstract fun LibraryDao(): LibraryDao
}