package ru.android.hikanumaruapp.utilits.room

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.google.gson.reflect.TypeToken
import ru.android.hikanumaruapp.model.Chapter
import ru.android.hikanumaruapp.model.MangaInfo

@ProvidedTypeConverter
class ConvertersRoom (
    private val jsonParser: GsonParser
) {
    @TypeConverter
    fun toChaptersJson(chapters: List<Chapter>) : String? {
        return jsonParser.toJson(
            chapters,
            object : TypeToken<ArrayList<Chapter>>(){}.type
        ) ?: "[]"
    }

    @TypeConverter
    fun fromChaptersJson(json: String): List<Chapter>?{
        return jsonParser.fromJson<ArrayList<Chapter>>(
            json,
            object: TypeToken<ArrayList<Chapter>>(){}.type
        ) ?: emptyList()
    }

    @TypeConverter
    fun toMangaInfoJson(mangaInfo: MangaInfo) : String? {
        return jsonParser.toJson(
            mangaInfo,
            object : TypeToken<MangaInfo>(){}.type
        ) ?: "[]"
    }

    @TypeConverter
    fun fromMangaInfoJson(json: String): MangaInfo?{
        return jsonParser.fromJson<MangaInfo>(
            json,
            object: TypeToken<MangaInfo>(){}.type
        ) ?: MangaInfo(
            "","","",
            "", mutableListOf(),mutableListOf(),mutableListOf(),
            "",mutableListOf(),)
    }
}