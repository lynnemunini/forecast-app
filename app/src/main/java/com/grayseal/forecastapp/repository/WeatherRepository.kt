package com.grayseal.forecastapp.repository

import com.grayseal.forecastapp.data.DataOrException
import com.grayseal.forecastapp.model.Weather
import com.grayseal.forecastapp.network.WeatherApi
import javax.inject.Inject

/*To allow getting the weather data from the WeatherAPI*/
class WeatherRepository @Inject constructor(private val api: WeatherApi) {
    suspend fun getWeather(
        latQuery: Double,
        lonQuery: Double,
    ): DataOrException<Weather, Boolean, Exception> {
        val response = try {
            api.getWeather(lat = latQuery, lon = lonQuery)

        } catch (e: Exception) {
            return DataOrException(e = e)
        }
        return DataOrException(data = response)
    }
}