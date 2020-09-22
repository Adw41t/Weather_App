package com.unique.weatherapp.data.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.unique.weatherapp.data.db.newentity.Hourly

@Dao
interface NewHourlyDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upsert(hourlyWeather: Hourly)

    @Query("select * from hourlyWeather")
    fun getHourlyWeather(): LiveData<List<Hourly>>

    @Query("delete from hourlyWeather")
    fun deleteHourlyWeather()

    @Query("select * from hourlyWeather limit 8")
    fun get8HourlyWeather(): LiveData<List<Hourly>>
}