package com.unique.weatherapp.data.providers

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.maps.model.LatLng
import com.unique.weatherapp.R
import com.unique.weatherapp.internal.LocationPermissionNotGrantedException
import com.unique.weatherapp.internal.asDeferred
import kotlinx.coroutines.Deferred
import java.io.IOException
import java.util.*


const val USE_DEVICE_LOCATION="USE_DEVICE_LOCATION"
const val CUSTOM_LOCATION="CUSTOM_LOCATION"
class locationProviderImpl(
    private val fusedLocationProviderClient: FusedLocationProviderClient,
    context: Context
) :
        PreferenceProvider(context), locationProvider {

    private val appContext=context.applicationContext
    private val ll:LatLng=LatLng(19.2183, 72.9781)
    private val defaultCity="Thane"
    override suspend fun hasLocationChanged(): Boolean {
        val deviceLocationChanged = try {
            hasDeviceLocationChanged(getCoordinates())
        }
        catch (e: LocationPermissionNotGrantedException){
            return false
        }
        return deviceLocationChanged || hasCustomLocationChanged(getCoordinates())
    }

    override suspend fun getpreferredLocationString(): String {
        if (isUsingDeviceLocation()){
            try {
                val deviceLocation = getLastDeviceLocation().await()
                        ?: return "${getCustomLocationName()}"
                return getCityNameFromCoordinates(LatLng(deviceLocation.latitude,deviceLocation.longitude))
            } catch (e: LocationPermissionNotGrantedException) {
                return "${getCustomLocationName()}"
            }
        }
        else{
            return "${getCustomLocationName()}"
        }
    }

    private suspend fun hasDeviceLocationChanged(lastWeatherLocation: LatLng): Boolean {
        if (!isUsingDeviceLocation())
            return false

        val deviceLocation = getLastDeviceLocation().await()
                ?: return false

        // Comparing doubles cannot be done with "=="
        val comparisonThreshold = 0.03
        return Math.abs(deviceLocation.latitude - lastWeatherLocation.latitude) > comparisonThreshold &&
                Math.abs(deviceLocation.longitude - lastWeatherLocation.longitude) > comparisonThreshold
    }

    private fun hasCustomLocationChanged(coordinates: LatLng): Boolean {
        if (!isUsingDeviceLocation()) {
            val customLocationName = getCustomLocationName()
            if(customLocationName!=null){
                return customLocationName.contentEquals(getCityNameFromCoordinates(coordinates))
            }
        }
        return false
    }

    private fun getCustomLocationName(): String? {
        return preferences.getString(CUSTOM_LOCATION, null)
    }
    private fun isUsingDeviceLocation(): Boolean {
        return preferences.getBoolean(USE_DEVICE_LOCATION, true)
    }

    @SuppressLint("MissingPermission")
    private fun getLastDeviceLocation():Deferred<android.location.Location?>{
        return if (hasLocationPermission())
            fusedLocationProviderClient.lastLocation.asDeferred()
        else
            throw LocationPermissionNotGrantedException()
    }

    private fun hasLocationPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            appContext,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    override suspend fun getpreferredLocationCoordinates(): LatLng {
        val cityName=getCustomLocationName()?:defaultCity
        if (isUsingDeviceLocation()){
            try {
                val deviceLocation = getLastDeviceLocation().await()
                if(deviceLocation!=null) {
                    return LatLng(deviceLocation.latitude, deviceLocation.longitude)
                }
                else{
                    return getCoordinatesFromCityName(cityName)
                }
            } catch (e: LocationPermissionNotGrantedException) {
                return getCoordinatesFromCityName(cityName)
            }
        }
        else{
            return getCoordinatesFromCityName(cityName)
        }
    }

    private fun getCoordinatesFromCityName(cityName: String):LatLng{

        if (Geocoder.isPresent()) {
            try {
                val gc = Geocoder(appContext)
                val addresses: List<Address> = gc.getFromLocationName(cityName, 1) // get the found Address Objects
                for (a in addresses) {
                    if (a.hasLatitude() && a.hasLongitude()) {
                        return LatLng(a.getLatitude(), a.getLongitude())
                    }
                }
                return ll
            } catch (e: IOException) {
                // handle the exception
                return ll
            }
        }
        return ll
    }

    private fun getCityNameFromCoordinates(coordinates: LatLng):String{
        val gcd = Geocoder(appContext, Locale.getDefault())
        val addresses = gcd.getFromLocation(coordinates.latitude, coordinates.longitude, 1)
        if (addresses.size > 0) {
            return addresses[0]?.locality?:defaultCity
        } else {
            return defaultCity
        }
    }
    override fun saveCoordinates(latLon: LatLng){
        preferences.edit().putFloat(
            appContext.getString(R.string.latitude_value),
            latLon.latitude.toFloat()
        ).apply()
        preferences.edit().putFloat(
            appContext.getString(R.string.latitude_value),
            latLon.longitude.toFloat()
        ).apply()
    }
    private fun getCoordinates():LatLng{
        return LatLng(
            preferences.getFloat(
                appContext.getString(R.string.latitude_value),
                ll.latitude.toFloat()
            ).toDouble(),
            preferences.getFloat(
                appContext.getString(R.string.latitude_value),
                ll.longitude.toFloat()
            ).toDouble()
        )
    }
}