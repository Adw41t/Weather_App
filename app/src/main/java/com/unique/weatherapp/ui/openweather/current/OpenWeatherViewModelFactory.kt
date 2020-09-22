package com.unique.weatherapp.ui.openweather.current

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.unique.weatherapp.data.providers.UnitProvider
import com.unique.weatherapp.data.repository.OpenWeatherRepository

class OpenWeatherViewModelFactory(
    private val openWeatherRepository: OpenWeatherRepository,
    private val unitProvider: UnitProvider
): ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return OpenWeatherViewModel(openWeatherRepository,unitProvider) as T
    }
}