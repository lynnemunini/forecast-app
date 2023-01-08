package com.grayseal.forecastapp.screens.forecast

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.grayseal.forecastapp.model.CurrentWeatherObject
import com.grayseal.forecastapp.repository.WeatherDbRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ForecastViewModel @Inject constructor(private val repository: WeatherDbRepository) :
    ViewModel() {
    private val _weatherObjectList = MutableStateFlow<List<CurrentWeatherObject>>(emptyList())
    val weatherObjectList = _weatherObjectList.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getWeatherObjects().distinctUntilChanged()
                .collect { listOfObjects ->
                    if (listOfObjects.isNullOrEmpty()) {
                        Log.d("TAG", "Empty favs")
                    } else {
                        _weatherObjectList.value = listOfObjects
                        Log.d("TAG", "${weatherObjectList.value}")
                    }
                }
        }
    }

    suspend fun getWeatherById(id: Int): CurrentWeatherObject {
        return repository.getWeatherById(id)
    }
    fun insertCurrentWeatherObject(currentWeatherObject: CurrentWeatherObject) = viewModelScope.launch { repository.insertCurrentWeatherObject(currentWeatherObject) }
    fun updateCurrentWeatherObject(currentWeatherObject: CurrentWeatherObject) = viewModelScope.launch { repository.updateCurrentWeatherObject(currentWeatherObject) }
}