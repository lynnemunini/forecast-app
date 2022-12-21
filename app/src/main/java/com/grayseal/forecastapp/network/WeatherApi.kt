package com.grayseal.forecastapp.network

import com.grayseal.forecastapp.model.WeatherObject
import retrofit2.http.GET
import javax.inject.Singleton


// To prevent creation of different instances of itself
@Singleton
interface WeatherApi {
    @GET(value = "data/2.5/onecall")
    suspend fun getWeather(

    ): WeatherObject
}