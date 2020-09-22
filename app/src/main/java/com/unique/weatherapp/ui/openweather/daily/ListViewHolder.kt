package com.unique.weatherapp.ui.openweather.daily

import android.content.Context
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.unique.weatherapp.R
import com.unique.weatherapp.data.db.newentity.Daily
import com.unique.weatherapp.internal.UnitSystem
import java.text.SimpleDateFormat
import java.util.*

class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val list_weather_icon=itemView.findViewById<ImageView>(R.id.list_weather_item_icon)
    val list_weather_Temp=itemView.findViewById<TextView>(R.id.list_weather_item_temp)
    val list_weather_wind=itemView.findViewById<TextView>(R.id.list_weather_item_wind)
    val list_weather_Time=itemView.findViewById<TextView>(R.id.list_weather_item_time)
    val list_weather_condition=itemView.findViewById<TextView>(R.id.list_weather_item_condition)
    val list_weather_clouds=itemView.findViewById<TextView>(R.id.list_weather_item_clouds)
    val list_weather_precipitation_probability=itemView.findViewById<TextView>(R.id.list_weather_item_rain)

    fun bindView(daily: Daily, context: Context, unitSystem: UnitSystem){
        list_weather_condition.text=daily.weather.get(0).description
        Glide.with(context)
                .load("http://openweathermap.org/img/wn/${daily.weather.get(0).icon}@2x.png")
                .into(list_weather_icon)
        list_weather_Temp.text=" Max :${daily.temp.max} " +
                "${chooseLocalizedUnitAbbreviation(unitSystem, "°C", "°F")}" +
                " Min: ${daily.temp.min} " +
                "${chooseLocalizedUnitAbbreviation(unitSystem, "°C", "°F")}"
        list_weather_wind.text="Wind: ${daily.windSpeed} ${chooseLocalizedUnitAbbreviation(unitSystem, "metre/sec", "miles/hour")}"

        list_weather_Time.text=convertTimeFromUTC(daily.dt)
        list_weather_clouds.text="Clouds: ${daily.clouds}%"
        list_weather_precipitation_probability.text="${(daily.pop *100).toInt()}%"
    }
    private fun chooseLocalizedUnitAbbreviation(unitSystem: UnitSystem, metric: String, imperial: String): String {
        return if (unitSystem.name.equals(UnitSystem.metric.name)) metric else imperial
    }

    private fun convertTimeFromUTC(time:Int):String{

        val cal = Calendar.getInstance()
        val tz = cal.timeZone
        val date = Date(time.toLong() * 1000)
        val fmt = SimpleDateFormat(("dd/MM/yyyy"))
        fmt.timeZone=tz
        return fmt.format(date)
    }
}