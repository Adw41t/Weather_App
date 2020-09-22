package com.unique.weatherapp.data.providers

import com.google.android.gms.maps.model.LatLng

interface locationProvider {

    suspend fun hasLocationChanged():Boolean

    suspend fun getpreferredLocationString():String

    suspend fun getpreferredLocationCoordinates(): LatLng

    fun saveCoordinates(latLon:LatLng)

}