package com.unique.weatherapp.ui.openweather.daily

import androidx.lifecycle.ViewModel
import com.unique.weatherapp.data.providers.UnitProvider
import com.unique.weatherapp.data.repository.OpenWeatherRepository
import com.unique.weatherapp.internal.UnitSystem
import com.unique.weatherapp.internal.lazyDeferred

class ListViewModel (
    private val openWeatherRepository: OpenWeatherRepository,
    unitProvider: UnitProvider
): ViewModel() {
    val unitSystem=unitProvider.unitSystem()//UnitSystem.METRIC //get from settings

    val isMetric:Boolean
        get() = unitSystem== UnitSystem.metric

    val dailyWeather by lazyDeferred {
        openWeatherRepository.getDailyWeather(unitSystem.name)
    }
    val locationName by lazyDeferred {
        openWeatherRepository.getLocationName()
    }
}