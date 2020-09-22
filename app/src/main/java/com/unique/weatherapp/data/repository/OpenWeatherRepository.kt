package com.unique.weatherapp.data.repository

import androidx.lifecycle.LiveData
import com.unique.weatherapp.data.db.newentity.Current
import com.unique.weatherapp.data.db.newentity.Daily
import com.unique.weatherapp.data.db.newentity.Hourly

interface OpenWeatherRepository {

    suspend fun getCurrentWeather(unit:String) : LiveData<Current>

    suspend fun getDailyWeather(unit:String) : LiveData<List<Daily>>

    suspend fun getHourlyWeather(unit:String) : LiveData<List<Hourly>>

    suspend fun getHourlyWeather8(unit:String) : LiveData<List<Hourly>>

    suspend fun getLocationName():String

    suspend fun refreshWeather(unit:String) : LiveData<Current>
}