package com.grayseal.forecastapp.navigation

enum class WeatherScreens {
    SplashScreen,
    ForecastScreen,
    SearchScreen,
    WeatherScreen,
    SettingScreen;

    companion object {
        fun fromRoute(route: String?): WeatherScreens = when (route?.substringBefore("/")) {
            SplashScreen.name -> SplashScreen
            WeatherScreen.name -> WeatherScreen
            ForecastScreen.name -> ForecastScreen
            SearchScreen.name -> SearchScreen
            SettingScreen.name -> SettingScreen
            null -> SplashScreen
            else -> throw IllegalArgumentException("Route $route is not recognized")
        }
    }
}