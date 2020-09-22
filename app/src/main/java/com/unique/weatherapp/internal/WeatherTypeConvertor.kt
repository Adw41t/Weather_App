package com.unique.weatherapp.internal

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.unique.weatherapp.data.network.response.Weather
import java.lang.reflect.Type
import java.util.*


class WeatherTypeConvertor {
    private val gson = Gson()
    @TypeConverter
    fun stringToList(data: String?): List<Weather> {
        if (data == null) {
            return Collections.emptyList()
        }
        val listType: Type = object : TypeToken<List<Weather>>() {}.type
        return gson.fromJson<List<Weather>>(data, listType)
    }

    @TypeConverter
    fun ListToString(someObjects: List<Weather>): String {
        return gson.toJson(someObjects)
    }
}