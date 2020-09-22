package com.unique.weatherapp.data.network.response


import com.google.gson.annotations.SerializedName

data class Weather(
    @SerializedName("description")
    val description: String,
    @SerializedName("icon")
    val icon: String,
    @SerializedName("id")
    val weatherId: Int,
    @SerializedName("main")
    val main: String
)