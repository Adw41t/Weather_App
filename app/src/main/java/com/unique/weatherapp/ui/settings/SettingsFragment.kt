package com.unique.weatherapp.ui.settings

import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.unique.weatherapp.R
import com.unique.weatherapp.data.providers.ThemeProvider

class SettingsFragment : PreferenceFragmentCompat() {
    private val themeProvider by lazy { ThemeProvider(requireContext()) }
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.preferences)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val screen = getPreferenceScreen()
        val listPreference=findPreference<ListPreference>(getString(R.string.theme_preferences_key))
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.Q) {
            screen.removePreference(listPreference)
        }
        listPreference?.setOnPreferenceChangeListener { preference, newValue ->
            listPreference.value = newValue.toString()
            if (newValue is String) {
                val theme = themeProvider.getTheme(newValue)
                AppCompatDelegate.setDefaultNightMode(theme)
            }
            true
        }
        listPreference?.summaryProvider = getThemeSummaryProvider()
    }

    private fun getThemeSummaryProvider() =
        Preference.SummaryProvider<ListPreference> { preference ->
            themeProvider.getThemeDescriptionForPreference(preference.value)
        }
}