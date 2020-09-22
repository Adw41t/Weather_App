package com.unique.weatherapp.ui.openweather.daily

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.unique.weatherapp.R
import com.unique.weatherapp.data.db.newentity.Daily
import com.unique.weatherapp.internal.UnitSystem

class ListAdapter(val list: List<Daily>, val unitSystem: UnitSystem, val context: Context): RecyclerView.Adapter<ListViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.list_weather_item,parent,false)

        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bindView(list.get(position),context,unitSystem)
    }

    override fun getItemCount(): Int {
       return list.size
    }
}