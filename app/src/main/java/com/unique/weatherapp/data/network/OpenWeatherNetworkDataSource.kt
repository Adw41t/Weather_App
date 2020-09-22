package com.unique.weatherapp.data.network

import androidx.lifecycle.LiveData
import com.unique.weatherapp.data.network.response.openWeatherResponse

interface OpenWeatherNetworkDataSource {
    val downloadedOpenWeather : LiveData<openWeatherResponse>

    suspend fun fetchOpenWeather(
            lat:Double,
            lon:Double,
            unit:String
    )
}