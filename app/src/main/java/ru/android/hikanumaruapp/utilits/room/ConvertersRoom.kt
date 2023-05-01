package ru.android.hikanumaruapp.utilits.room

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.google.gson.reflect.TypeToken
import ru.android.hikanumaruapp.data.model.Chapter
import ru.android.hikanumaruapp.data.model.GenresMainModel
import ru.android.hikanumaruapp.data.model.MangaInfo
import ru.android.hikanumaruapp.data.model.MangaListCoverLinks

@ProvidedTypeConverter
class ConvertersRoom (
    private val jsonParser: GsonParser
) {

    @TypeConverter
    fun toStringArray(list: List<String>) : String? {
        return jsonParser.toJson(
            list,
            object : TypeToken<ArrayList<String>>(){}.type
        ) ?: "[]"
    }

    @TypeConverter
    fun fromStringArray(json: String): List<String>?{
        return jsonParser.fromJson<ArrayList<String>>(
            json,
            object: TypeToken<ArrayList<String>>(){}.type
        ) ?: emptyList()
    }

    @TypeConverter
    fun toGenresMainModelJson(genresMainModel: List<GenresMainModel>) : String? {
        return jsonParser.toJson(
            genresMainModel,
            object : TypeToken<ArrayList<GenresMainModel>>(){}.type
        ) ?: "[]"
    }

    @TypeConverter
    fun fromGenresMainModelJson(json: String): List<GenresMainModel>?{
        return jsonParser.fromJson<ArrayList<GenresMainModel>>(
            json,
            object: TypeToken<ArrayList<GenresMainModel>>(){}.type
        ) ?: emptyList()
    }

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
    fun toMangaListCoverLinksJson(json: MangaListCoverLinks) : String? {
        return jsonParser.toJson(
            json,
            object : TypeToken<MangaListCoverLinks>(){}.type
        ) ?: "[]"
    }

    @TypeConverter
    fun fromMangaListCoverLinksJson(json: String): MangaListCoverLinks?{
        return jsonParser.fromJson<MangaListCoverLinks>(
            json,
            object: TypeToken<MangaListCoverLinks>(){}.type
        ) ?: MangaListCoverLinks(null,null)
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