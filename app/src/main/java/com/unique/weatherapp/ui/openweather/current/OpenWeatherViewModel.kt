package com.unique.weatherapp.ui.openweather.current

import androidx.lifecycle.ViewModel
import com.unique.weatherapp.data.providers.UnitProvider
import com.unique.weatherapp.data.repository.OpenWeatherRepository
import com.unique.weatherapp.internal.UnitSystem
import com.unique.weatherapp.internal.lazyDeferred

class OpenWeatherViewModel(
    private val openWeatherRepository: OpenWeatherRepository,
    unitProvider: UnitProvider
) : ViewModel() {

     val unitSystem=unitProvider.unitSystem()//UnitSystem.METRIC //get from settings

    val isMetric:Boolean
        get() = unitSystem== UnitSystem.metric

    val weather by lazyDeferred {
        openWeatherRepository.getCurrentWeather(unitSystem.name)
    }

    val hourlyWeather8 by lazyDeferred {
        openWeatherRepository.getHourlyWeather8(unitSystem.name)
    }

    val locationName by lazyDeferred {
        openWeatherRepository.getLocationName()
    }

    val refreshWeather by lazyDeferred {
        openWeatherRepository.refreshWeather(unitSystem.name)
    }
}