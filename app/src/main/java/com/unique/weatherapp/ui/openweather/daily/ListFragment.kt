package com.unique.weatherapp.ui.openweather.daily

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.unique.weatherapp.R
import com.unique.weatherapp.data.db.newentity.Daily
import com.unique.weatherapp.ui.Base.ScopedFragment
import kotlinx.android.synthetic.main.list_fragment.*
import kotlinx.coroutines.launch
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance

class ListFragment : ScopedFragment(), KodeinAware {
    override val kodein: Kodein by closestKodein()
    private val viewModelFactory by instance<ListViewModelFactory>()
    private lateinit var viewModel: ListViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.list_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this,viewModelFactory).get(ListViewModel::class.java)
        bindUI()
    }

    private fun bindUI()=launch{
        val dailyWeather=viewModel.dailyWeather.await()
        dailyWeather.observe(viewLifecycleOwner, Observer{
            if(it==null)
                return@Observer

            loadDailyRecycler(it)
        })
        val locationName=viewModel.locationName.await()
    }

    private fun loadDailyRecycler(list:List<Daily>){
        val dailyWeatherAdapter= ListAdapter(list,viewModel.unitSystem, requireContext())
        val layoutManager = LinearLayoutManager(context)
        list_weather_recycler.layoutManager=layoutManager
        list_weather_recycler.adapter=dailyWeatherAdapter
    }

}