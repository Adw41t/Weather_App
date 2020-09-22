package com.unique.weatherapp.ui.openweather.hourly

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.unique.weatherapp.R
import com.unique.weatherapp.data.db.newentity.Hourly
import com.unique.weatherapp.internal.UnitSystem

class HourlyWeatherAdapter(val list: List<Hourly>, val unitSystem: UnitSystem, val context:Context,val weatherId:Int): RecyclerView.Adapter<HourlyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HourlyViewHolder {
        val view= LayoutInflater.from(context).inflate(R.layout.hourly_weather_item,parent,false)

        return HourlyViewHolder(view)
    }

    override fun onBindViewHolder(holder: HourlyViewHolder, position: Int) {
        holder.bindView(list.get(position),context,unitSystem,weatherId)
    }

    override fun getItemCount(): Int {
       return list.size
    }

}