package com.unique.weatherapp.data.providers

import com.unique.weatherapp.internal.UnitSystem

interface UnitProvider {
    fun unitSystem():UnitSystem
}