package com.unique.weatherapp.data.db.newentity


import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.google.gson.annotations.SerializedName
import com.unique.weatherapp.data.network.response.Weather
import com.unique.weatherapp.internal.WeatherTypeConvertor

const val CURRENT_WEATHER_ID=0
@Entity(tableName = "currentWeather")
data class Current(
    @SerializedName("clouds")
    val clouds: Int,
    @SerializedName("dew_point")
    val dewPoint: Double,
    @SerializedName("dt")
    val dt: Int,
    @SerializedName("feels_like")
    val feelsLike: Double,
    @SerializedName("humidity")
    val humidity: Int,
    @SerializedName("pressure")
    val pressure: Int,
    @SerializedName("sunrise")
    val sunrise: Int,
    @SerializedName("sunset")
    val sunset: Int,
    @SerializedName("temp")
    val temp: Double,
    @SerializedName("uvi")
    val uvi: Double,
    @SerializedName("visibility")
    val visibility: Int,
    @SerializedName("weather")
        @TypeConverters(WeatherTypeConvertor::class)
    val weather: List<Weather>,
    @SerializedName("wind_deg")
    val windDeg: Int,
    @SerializedName("wind_speed")
    val windSpeed: Double
){
    @PrimaryKey(autoGenerate = false)
    var id :Int = CURRENT_WEATHER_ID
}