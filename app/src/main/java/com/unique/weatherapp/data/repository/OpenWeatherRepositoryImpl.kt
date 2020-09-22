package com.unique.weatherapp.data.repository

import androidx.lifecycle.LiveData
import com.google.android.gms.maps.model.LatLng
import com.unique.weatherapp.data.db.NewCurrentDao
import com.unique.weatherapp.data.db.NewDailyDao
import com.unique.weatherapp.data.db.NewHourlyDao
import com.unique.weatherapp.data.db.newentity.Current
import com.unique.weatherapp.data.db.newentity.Daily
import com.unique.weatherapp.data.db.newentity.Hourly
import com.unique.weatherapp.data.network.OpenWeatherNetworkDataSource
import com.unique.weatherapp.data.network.response.openWeatherResponse
import com.unique.weatherapp.data.providers.locationProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.threeten.bp.Instant
import org.threeten.bp.LocalDateTime
import org.threeten.bp.ZoneId

class OpenWeatherRepositoryImpl(
    private val currentDao: NewCurrentDao,
    private val dailyDao: NewDailyDao,
    private val hourlyDao: NewHourlyDao,
    private val openWeatherNetworkDataSource: OpenWeatherNetworkDataSource,
    private val locationProvider: locationProvider
) : OpenWeatherRepository {

    init {
        openWeatherNetworkDataSource.downloadedOpenWeather.observeForever { openWeather->

            persistFetchedWeather(openWeather)
        }
    }
    private fun persistFetchedWeather(fetchedWeather : openWeatherResponse){
        GlobalScope.launch(Dispatchers.IO){
            saveLocationCoordinates(fetchedWeather.lat,fetchedWeather.lon)
            deleteCurrentWeather()
            deleteDailyWeather()
            deleteHourlyWeather()
            currentDao.upsert(fetchedWeather.current)
            for (daily in fetchedWeather.daily) {
                dailyDao.upsert(daily)
            }
            for (hourly in fetchedWeather.hourly) {
                hourlyDao.upsert(hourly)
            }
        }
    }

    override suspend fun refreshWeather(unit:String): LiveData<Current> {
        return withContext(Dispatchers.IO){
            fetchCurrentWeather(unit)
            return@withContext currentDao.getCurrentWeatherFromID()
        }
    }

    override suspend fun getCurrentWeather(unit:String): LiveData<Current> {
        return withContext(Dispatchers.IO){
            initWeatherData(unit)
            return@withContext currentDao.getCurrentWeatherFromID()
        }
    }

    override suspend fun getDailyWeather(unit:String): LiveData<List<Daily>> {
        return withContext(Dispatchers.IO){
            return@withContext dailyDao.getDailyWeather()
        }
    }

    override suspend fun getHourlyWeather(unit:String): LiveData<List<Hourly>> {
        return withContext(Dispatchers.IO){
            return@withContext hourlyDao.getHourlyWeather()
        }
    }
    override suspend fun getHourlyWeather8(unit:String): LiveData<List<Hourly>> {
        return withContext(Dispatchers.IO){
            return@withContext hourlyDao.get8HourlyWeather()
        }
    }

    override suspend fun getLocationName(): String {
        return withContext(Dispatchers.IO){
            locationProvider.getpreferredLocationString()
        }
    }
    private suspend fun initWeatherData(unit: String){
        val current=    currentDao.getCurrentWeather()

        if(current==null || locationProvider.hasLocationChanged()){
            fetchCurrentWeather(unit)
            return
        }

        if (isFetchCurrentNeeded(LocalDateTime.ofInstant(Instant.ofEpochSecond(current.dt.toLong()), ZoneId.systemDefault()))) {
            fetchCurrentWeather(unit)
        }

    }
    private suspend fun fetchCurrentWeather(unit: String){
        val latlong :LatLng = locationProvider.getpreferredLocationCoordinates()
        openWeatherNetworkDataSource.fetchOpenWeather(latlong.latitude,latlong.longitude,unit)
    }
    private fun isFetchCurrentNeeded(lastFetchTime :LocalDateTime) : Boolean{
        val sixtyminutesago=LocalDateTime.now().minusMinutes(60)
        return lastFetchTime.isBefore(sixtyminutesago)
    }
    private fun deleteCurrentWeather(){
        currentDao.deleteCurrentWeather()
    }
    private fun deleteDailyWeather(){
        dailyDao.deleteDailyWeather()
    }
    private fun deleteHourlyWeather(){
        hourlyDao.deleteHourlyWeather()
    }

    private fun saveLocationCoordinates(lat:Double, lon:Double){
        locationProvider.saveCoordinates(LatLng(lat,lon))
    }
}