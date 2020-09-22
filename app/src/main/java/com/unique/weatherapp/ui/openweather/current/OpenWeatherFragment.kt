package com.unique.weatherapp.ui.openweather.current

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.bumptech.glide.Glide
import com.unique.weatherapp.R
import com.unique.weatherapp.data.db.newentity.Hourly
import com.unique.weatherapp.ui.Base.ScopedFragment
import com.unique.weatherapp.ui.openweather.hourly.HourlyWeatherAdapter
import kotlinx.android.synthetic.main.open_weather_fragment.*
import kotlinx.coroutines.launch
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance

class OpenWeatherFragment : ScopedFragment(), KodeinAware {

    override val kodein: Kodein by closestKodein()
    private val viewModelFactory by instance<OpenWeatherViewModelFactory>()

    private lateinit var viewModel: OpenWeatherViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.open_weather_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this,viewModelFactory).get(OpenWeatherViewModel::class.java)
        open_swipe_refresh.isRefreshing=true
        bindUI(false)
        open_swipe_refresh.setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener {
            bindUI(true)
        })
    }

    private fun bindUI(refreshWeather:Boolean) = launch {
        var weatherId=0
        val currentWeather = if(refreshWeather)
            viewModel.refreshWeather.await()
        else
            viewModel.weather.await()
        currentWeather.observe(viewLifecycleOwner, Observer {
            open_swipe_refresh.isRefreshing=false
            if(it == null) return@Observer
            updateTemperatures(it.temp, it.feelsLike)
            updateCondition(it.weather.get(0).description)
            updateHumidity(it.windSpeed)
            updateClouds(it.clouds)
            updateWindSpeed(it.windSpeed)
            weatherId=it.weather.get(0).weatherId
            Glide.with(this@OpenWeatherFragment)
                    .load("http://openweathermap.org/img/wn/${it.weather.get(0).icon}@2x.png")
                    .into(open_imageView_condition_icon)
        })
        val locationName=viewModel.locationName.await()
        updateLocationName(locationName)
        val hourlyWeather=viewModel.hourlyWeather8.await()

        hourlyWeather.observe(viewLifecycleOwner, Observer {
            if(it == null) return@Observer
            loadHourlyRecycler(it,weatherId)
        })
    }
    private fun chooseLocalizedUnitAbbreviation(metric: String, imperial: String): String {
        return if (viewModel.isMetric) metric else imperial
    }
    private fun updateLocationName(locationName:String) {
        open_textView_city.text=locationName
    }

    private fun updateTemperatures(temperature: Double, feelsLike: Double) {
        val unitAbbreviation = chooseLocalizedUnitAbbreviation("°C", "°F")
        open_textView_temperature.text = "$temperature$unitAbbreviation"
        open_textView_feels_like_temperature.text = "Feels like $feelsLike$unitAbbreviation"
    }

    private fun updateCondition(condition: String) {
        open_textView_condition.text = condition
    }

    private fun loadHourlyRecycler(list:List<Hourly>, weatherId:Int){
        val hourlyWeatherAdapter= HourlyWeatherAdapter(list,viewModel.unitSystem, requireContext(),weatherId)
        val layoutManager = GridLayoutManager(context,4)
        open_weather_recycler.layoutManager=layoutManager
        open_weather_recycler.adapter=hourlyWeatherAdapter
    }

    private fun updateHumidity(humidity: Double) {
        open_textView_humidity.text = "Humidity : $humidity %"
    }
    private fun updateWindSpeed(windSpeed: Double) {
        val unitAbbreviation = chooseLocalizedUnitAbbreviation("metre/sec", "miles/hour")
        open_textView_wind_speed.text = "Wind Speed : $windSpeed $unitAbbreviation"
    }
    private fun updateClouds(clouds: Int) {
        open_textView_clouds.text = "Clouds : $clouds %"
    }
}