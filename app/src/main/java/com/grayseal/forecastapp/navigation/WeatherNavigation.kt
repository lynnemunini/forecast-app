package com.grayseal.forecastapp.navigation

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.grayseal.forecastapp.screens.ForecastScreen
import com.grayseal.forecastapp.screens.LocationScreen
import com.grayseal.forecastapp.screens.SettingScreen
import com.grayseal.forecastapp.screens.SplashScreen
import com.grayseal.forecastapp.screens.main.MainViewModel
import com.grayseal.forecastapp.screens.main.WeatherScreen

@Composable
fun WeatherNavigation(context: Context) {
    val navController = rememberNavController()
    val mainViewModel = hiltViewModel<MainViewModel>()
    NavHost(navController = navController, startDestination = WeatherScreens.SplashScreen.name) {
        composable(WeatherScreens.SplashScreen.name) {
            SplashScreen(navController = navController)
        }
        composable(WeatherScreens.WeatherScreen.name) {
            WeatherScreen(navController = navController, mainViewModel, context)
        }
        composable(WeatherScreens.ForecastScreen.name) {
            ForecastScreen(navController = navController, mainViewModel)
        }
        composable(WeatherScreens.LocationScreen.name) {
            LocationScreen(navController = navController, mainViewModel)
        }
        composable(WeatherScreens.SettingScreen.name) {
            SettingScreen(navController = navController)
        }
    }
}