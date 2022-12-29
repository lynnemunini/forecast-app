package com.grayseal.forecastapp.screens.main

import android.content.Context
import android.content.pm.PackageManager
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.grayseal.forecastapp.data.DataOrException
import com.grayseal.forecastapp.location.getCurrentLocation
import com.grayseal.forecastapp.location.requestLocationPermissions
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

    fun requestLocationPermission(context: Context) {
        requestLocationPermissions(context)
    }

    fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray,
        context: Context
    ) {
        // Check if the location permission was granted
        if (requestCode == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
            // The location permission was granted, so get the user's location
            getCurrentLocation(context) { location ->
                // Update the latitude and longitude values
                latitude.value = location.latitude
                longitude.value = location.longitude
            }
        } else {
            // The location permission was denied, so show the permissions rationale
        }
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
}
