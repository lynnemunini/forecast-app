package com.grayseal.forecastapp.screens.main

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.grayseal.forecastapp.data.DataOrException
import com.grayseal.forecastapp.model.Weather
import com.grayseal.forecastapp.repository.WeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val repository: WeatherRepository) : ViewModel() {
    val data: MutableState<DataOrException<Weather, Boolean, Exception>> = mutableStateOf(
        DataOrException(null, true, Exception(""))
    )

    init {
        loadWeather()
    }

    private fun loadWeather() {
        getWeather(-1.23926, -36.890315)
    }

    private fun getWeather(lat: Double, lon: Double) {
        viewModelScope.launch {
            if (lat.isNaN()) return@launch
            if (lon.isNaN()) return@launch
            data.value.loading = true
            data.value = repository.getWeather(latQuery = lat, lonQuery = lon)
            if (data.value.data.toString().isNotEmpty()) data.value.loading = false
        }
        Log.d("Weather API", "getWeather: ${data.value.data.toString()}")
    }
}