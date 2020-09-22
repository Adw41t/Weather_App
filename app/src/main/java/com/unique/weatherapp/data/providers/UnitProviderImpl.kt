package com.unique.weatherapp.data.providers

import android.content.Context
import com.unique.weatherapp.internal.UnitSystem

class UnitProviderImpl (
        context:Context
): PreferenceProvider(context), UnitProvider {

    override fun unitSystem():UnitSystem {
        val selectedName = preferences.getString("UNIT_SYSTEM" ,UnitSystem.metric.name)?:UnitSystem.metric.name
        return UnitSystem.valueOf(selectedName)

    }
}