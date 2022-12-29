package com.grayseal.forecastapp.screens.main

import android.content.Context
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.grayseal.forecastapp.data.DataOrException
import com.grayseal.forecastapp.model.Weather

@Composable
fun WeatherScreen(navController: NavController, mainViewModel: MainViewModel, context: Context) {
    val latitude = mainViewModel.getCurrentLatitude(context = context)
    val longitude = mainViewModel.getCurrentLongitude(context = context)

    val gradientColors = listOf(Color(0xFF060620), colors.primary)
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.linearGradient(
                    colors = gradientColors,
                    start = Offset(0f, Float.POSITIVE_INFINITY),
                    end = Offset(Float.POSITIVE_INFINITY, 0f)
                )
            )
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            ShowData(
                mainViewModel = mainViewModel,
                latitude = latitude,
                longitude = longitude,
                context = context
            )
        }
    }
}

@Composable
fun ShowData(mainViewModel: MainViewModel, latitude: Double, longitude: Double, context: Context) {
    if (latitude != 360.0 && longitude != 360.0) {
        // Latitude and longitude are valid, so continue as normal
        val weatherData = produceState<DataOrException<Weather, Boolean, Exception>>(
            initialValue = DataOrException(loading = true)
        ) {
            Log.d("Lat $ Lon", "$latitude and $longitude")
            value = mainViewModel.getWeatherData(latitude, longitude)
        }.value


        if (weatherData.loading == true) {
            CircularProgressIndicator()
            Text("Fetching Weather data")
        } else if (weatherData.data != null) {
            Text(text = weatherData.data!!.current.weather[0].description, color = Color.White)
        }
    } else {
        // Latitude and longitude are not valid, show empty mainScreen
        Text("Main Screen", color = Color.White, fontSize = 18.sp)
    }
}
