package com.unique.weatherapp.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.unique.weatherapp.data.db.newentity.Current
import com.unique.weatherapp.data.db.newentity.Daily
import com.unique.weatherapp.data.db.newentity.Hourly
import com.unique.weatherapp.internal.WeatherTypeConvertor

@Database(
        entities=[Current::class, Daily::class, Hourly::class],
        version=1
)
@TypeConverters(WeatherTypeConvertor::class)
abstract class OpenWeatherDatabase: RoomDatabase()  {
    abstract fun newCurrentDao():NewCurrentDao
    abstract fun newDailyDao() : NewDailyDao
    abstract fun newHourlyDao() : NewHourlyDao

    companion object{
        @Volatile private var instance : OpenWeatherDatabase?=null
        //so that no two threads access at same time
        private val LOCK=Any()
        operator fun invoke(context: Context)= instance ?: synchronized(LOCK){
            instance?: buildDatabase(context).also { instance=it }
        }

        private fun buildDatabase(context: Context)=
                Room.databaseBuilder(context.applicationContext,
                        OpenWeatherDatabase::class.java,"newWeather.db")
                        .build()
    }
}