package com.unique.weatherapp.data.network

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.unique.weatherapp.BuildConfig
import com.unique.weatherapp.data.network.response.openWeatherResponse
import kotlinx.coroutines.Deferred
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

const val NEW_API_KEY= BuildConfig.API_KEY

interface OpenWeatherApiService {
    @GET(value = "onecall")
    fun getOpenWeather(
            @Query(value = "lat") lat :Double,
            @Query(value = "lon") lon :Double,
            @Query(value = "exclude") exclude:String="minutely",
            @Query(value = "units") units:String="metric"
    ): Deferred<openWeatherResponse>

    companion object{
        operator fun invoke(
                connectivityInterceptor: ConnectivityInterceptor
        ): OpenWeatherApiService {
            val requestInterceptor= Interceptor{chain->
                val url=chain.request()
                        .url()
                        .newBuilder()
                        .addQueryParameter("appid", NEW_API_KEY)
                        .build()
                val request=chain.request()
                        .newBuilder()
                        .url(url)
                        .build()
                return@Interceptor chain.proceed(request)
            }

            val okHttpClient= OkHttpClient.Builder()
                    .addInterceptor(requestInterceptor)
                    .addInterceptor(connectivityInterceptor)
                    .build()

            return Retrofit.Builder()
                    .client(okHttpClient)
                    .baseUrl("https://api.openweathermap.org/data/2.5/")
                    .addCallAdapterFactory(CoroutineCallAdapterFactory())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                    .create(OpenWeatherApiService::class.java)
        }
    }
}