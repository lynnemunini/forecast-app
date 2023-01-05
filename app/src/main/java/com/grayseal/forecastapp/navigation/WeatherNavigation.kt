package com.grayseal.forecastapp.navigation

import android.content.Context
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.grayseal.forecastapp.data.DataOrException
import com.grayseal.forecastapp.model.Weather
import com.grayseal.forecastapp.screens.SettingScreen
import com.grayseal.forecastapp.screens.forecast.ForecastScreen
import com.grayseal.forecastapp.screens.search.LocationScreen
import com.grayseal.forecastapp.screens.main.MainViewModel
import com.grayseal.forecastapp.screens.main.WeatherScreen
import com.grayseal.forecastapp.screens.splash.SplashScreen

@Composable
fun WeatherNavigation(context: Context) {
    val navController = rememberNavController()
    val mainViewModel = hiltViewModel<MainViewModel>()
    val latitude = remember {
        mutableStateOf(360.0)
    }
    val longitude = remember {
        mutableStateOf(360.0)
    }
    val weatherData = produceState<DataOrException<Weather, Boolean, Exception>>(
        initialValue = DataOrException(loading = true)
    ) {
        Log.d("Lat $ Lon", "$latitude and $longitude")
        value = mainViewModel.getWeatherData(latitude.value, longitude.value)
    }.value
    NavHost(navController = navController, startDestination = WeatherScreens.SplashScreen.name) {
        composable(WeatherScreens.SplashScreen.name) {
            SplashScreen(navController = navController)
        }
        composable(WeatherScreens.WeatherScreen.name) {
            WeatherScreen(
                mainViewModel,
                navController,
                context,
                latitude,
                longitude
            )
        }
        composable(WeatherScreens.ForecastScreen.name) {
            ForecastScreen(navController = navController, weatherData)
        }
        composable(WeatherScreens.LocationScreen.name) {
            LocationScreen(navController = navController, mainViewModel)
        }
        composable(WeatherScreens.SettingScreen.name) {
            SettingScreen(navController = navController)
        }
    }
}