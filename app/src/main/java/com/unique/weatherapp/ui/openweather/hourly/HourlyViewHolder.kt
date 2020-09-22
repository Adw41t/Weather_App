package com.unique.weatherapp.ui.openweather.hourly

import android.content.Context
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.unique.weatherapp.R
import com.unique.weatherapp.data.db.newentity.Hourly
import com.unique.weatherapp.internal.UnitSystem
import java.text.SimpleDateFormat
import java.util.*

class HourlyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val hourlyWeatherIcon=itemView.findViewById<ImageView>(R.id.hourly_weather_item_icon)
    val hourlyTemp=itemView.findViewById<TextView>(R.id.hourly_weather_item_temp)
    val hourlyTime=itemView.findViewById<TextView>(R.id.hourly_weather_item_time)
    val hourlyPrecipitationProbability=itemView.findViewById<TextView>(R.id.hourly_weather_item_rain)

    fun bindView(hourly: Hourly, context: Context, unitSystem: UnitSystem,weatherId:Int){

        Glide.with(context)
                .load("http://openweathermap.org/img/wn/${hourly.weather.get(0).icon}@2x.png")
                .into(hourlyWeatherIcon)
        hourlyTemp.text="${hourly.temp} ${chooseLocalizedUnitAbbreviation(unitSystem, "°C", "°F")}"

        hourlyTime.text=convertTimeFromUTC(hourly.dt)
        hourlyPrecipitationProbability.text="${(hourly.pop *100).toInt()}%"
    }
    private fun chooseLocalizedUnitAbbreviation(unitSystem: UnitSystem, metric: String, imperial: String): String {
        return if (unitSystem.name.equals(UnitSystem.metric.name)) metric else imperial
    }

    private fun convertTimeFromUTC(time:Int):String{

        val cal = Calendar.getInstance()
        val tz = cal.timeZone
        val date = Date(time.toLong() * 1000)
        val fmt = SimpleDateFormat(("hh:mm:aa"))
        fmt.timeZone=tz
        return fmt.format(date)
    }

}