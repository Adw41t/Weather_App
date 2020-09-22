package com.unique.weatherapp.data.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.unique.weatherapp.data.db.newentity.CURRENT_WEATHER_ID
import com.unique.weatherapp.data.db.newentity.Current

@Dao
interface NewCurrentDao {
        @Insert(onConflict = OnConflictStrategy.REPLACE)
        fun upsert(weather: Current)

        @Query("select * from currentWeather where id=$CURRENT_WEATHER_ID")
        fun getCurrentWeatherFromID(): LiveData<Current>

        @Query("select * from currentWeather")
        fun getCurrentWeather(): Current?

        @Query("delete from currentWeather")
        fun deleteCurrentWeather()
}