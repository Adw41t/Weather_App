package com.unique.weatherapp.ui.openweather.daily

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.unique.weatherapp.data.providers.UnitProvider
import com.unique.weatherapp.data.repository.OpenWeatherRepository

class ListViewModelFactory(
    private val openWeatherRepository: OpenWeatherRepository,
    private val unitProvider: UnitProvider
): ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ListViewModel(openWeatherRepository,unitProvider) as T
    }
}