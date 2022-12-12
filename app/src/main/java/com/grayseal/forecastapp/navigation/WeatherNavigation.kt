package com.grayseal.forecastapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.grayseal.forecastapp.screens.*

@Composable
fun WeatherNavigation(){
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = WeatherScreens.SplashScreen.name){
        composable(WeatherScreens.SplashScreen.name){
            SplashScreen(navController = navController)
        }
        composable(WeatherScreens.WeatherScreen.name){
            WeatherScreen(navController = navController)
        }
        composable(WeatherScreens.ForecastScreen.name){
            ForecastScreen(navController = navController)
        }
        composable(WeatherScreens.LocationScreen.name){
            LocationScreen(navController = navController)
        }
        composable(WeatherScreens.SettingScreen.name){
            SettingScreen(navController = navController)
        }
    }
}