package ru.android.hikanumaruapp.presentasion.reader.data

import android.content.Context

class ApplicationSettingsProvider(val context: Context) {

    object SourceProvider {
        const val READMANGA_SOURCE = "readmanga"
        const val MANGALIB_SOURCE = "mangalib"
        const val RANOBELIB_SOURCE = "ranobelib"

        const val SELECTED_SOURCE = "readmanga"
    }

    companion object {
        const val SETTINGS_PREFERENCE = "settings_preferences"

        const val SETTINGS_SOURCE = "settings_source_name"
    }

    fun source(source: String? = null): String?{
        if (source != null)
            context.getSharedPreferences(SETTINGS_PREFERENCE, Context.MODE_PRIVATE)
                .edit().putString(SETTINGS_SOURCE, source).apply()
        else
             return context.getSharedPreferences(SETTINGS_PREFERENCE, Context.MODE_PRIVATE)
                .getString(SETTINGS_SOURCE, SourceProvider.READMANGA_SOURCE)
        return null
    }

}