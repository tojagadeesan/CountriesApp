package com.example.countries.db

import androidx.room.TypeConverter
import com.example.countries.model.Currency
import com.example.countries.model.Language
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class CountriesTypeConverter {
    @TypeConverter
    fun toString(value: List<Currency>?): String? {
        return if (value == null) null else Gson().toJson(value)
    }

    @TypeConverter
    fun toList(value: String?): List<Currency>? {
        return (if (value == null) null else Gson().fromJson(
            value,
            object : TypeToken<List<Currency>>() {}.type
        ))
    }

    @TypeConverter
    fun toLaungageString(value: List<Language>?): String? {
        return if (value == null) null else Gson().toJson(value)
    }

    @TypeConverter
    fun toLaungagueList(value: String?): List<Language>? {
        return (if (value == null) null else Gson().fromJson(
            value,
            object : TypeToken<List<Language>>() {}.type
        ))
    }

}