package com.unique.weatherapp.data.network

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.unique.weatherapp.data.network.response.openWeatherResponse
import com.unique.weatherapp.internal.NoConnectivityException

class OpenWeatherNetworkDataSourceImpl (
        private val openWeatherApiService: OpenWeatherApiService
): OpenWeatherNetworkDataSource {
    private val _downloadedOpenWeather=MutableLiveData<openWeatherResponse>()
    override val downloadedOpenWeather: LiveData<openWeatherResponse>
        get() = _downloadedOpenWeather

    override suspend fun fetchOpenWeather(lat: Double, lon: Double, unit:String) {
        try {
            val fetchResponse = openWeatherApiService.getOpenWeather(lat=lat, lon=lon,units=unit).await()

            _downloadedOpenWeather.postValue(fetchResponse)
        }
        catch (e: NoConnectivityException){
            Log.e("Connectivity","No Connectivity",e)
        }
    }
}