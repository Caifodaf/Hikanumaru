package ru.android.hikanumaruapp.utilits.room

import java.lang.reflect.Type

abstract class JsonParser {
    abstract fun <T> fromJson(json: String, type: Type): T?
    abstract fun <T> toJson(obj: T, type: Type): String?
}