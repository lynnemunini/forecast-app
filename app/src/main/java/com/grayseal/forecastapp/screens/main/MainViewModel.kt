package com.grayseal.forecastapp.screens.main

import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.content.Context
import android.content.pm.PackageManager
import androidx.compose.runtime.mutableStateOf
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import com.grayseal.forecastapp.data.DataOrException
import com.grayseal.forecastapp.location.getCurrentLocation
import com.grayseal.forecastapp.model.Weather
import com.grayseal.forecastapp.repository.WeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val repository: WeatherRepository) : ViewModel() {

    private var latitude = mutableStateOf(360.0)
    private var longitude = mutableStateOf(360.0)

    suspend fun getWeatherData(
        lat: Double,
        lon: Double
    ): DataOrException<Weather, Boolean, Exception> {
        return repository.getWeather(latQuery = lat, lonQuery = lon)
    }

    fun getCurrentLatitude(context: Context): Double {
        getCurrentLocation(context = context) { location ->
            latitude.value = location.latitude
        }
        return latitude.value
    }

    fun getCurrentLongitude(context: Context): Double {
        getCurrentLocation(context = context) { location ->
            longitude.value = location.longitude
        }
        return longitude.value
    }

    fun hasLocationPermission(context: Context): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

}
