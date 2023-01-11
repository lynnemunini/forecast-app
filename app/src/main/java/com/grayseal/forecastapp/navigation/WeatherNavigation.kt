package com.grayseal.forecastapp.navigation

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.grayseal.forecastapp.screens.SettingScreen
import com.grayseal.forecastapp.screens.forecast.ForecastScreen
import com.grayseal.forecastapp.screens.forecast.ForecastViewModel
import com.grayseal.forecastapp.screens.main.MainViewModel
import com.grayseal.forecastapp.screens.main.WeatherScreen
import com.grayseal.forecastapp.screens.search.FavouriteViewModel
import com.grayseal.forecastapp.screens.search.SearchScreen
import com.grayseal.forecastapp.screens.splash.SplashScreen
import com.grayseal.forecastapp.widgets.BottomNavItem

@Composable
fun WeatherNavigation(context: Context) {
    val navController = rememberNavController()
    val mainViewModel = hiltViewModel<MainViewModel>()
    val favouriteViewModel = hiltViewModel<FavouriteViewModel>()
    val forecastViewModel = hiltViewModel<ForecastViewModel>()

    NavHost(navController = navController, startDestination = Screen.Splash.route) {
        composable(Screen.Splash.route) {
            SplashScreen(navController = navController)
        }
        val route = BottomNavItem.Home.route
        composable("$route/{city}", arguments = listOf(navArgument(name = "city") {
            type = NavType.StringType
        })) { navBack ->
            navBack.arguments?.getString("city").let { city ->
                WeatherScreen(
                    navController = navController,
                    mainViewModel = mainViewModel,
                    forecastViewModel = forecastViewModel,
                    context = context,
                    city = city
                )
            }
        }
        composable(BottomNavItem.Forecast.route) {
            ForecastScreen(navController = navController, forecastViewModel)
        }
        composable(BottomNavItem.Search.route) {
            SearchScreen(navController = navController, context, favouriteViewModel, mainViewModel)
        }
        composable(BottomNavItem.Settings.route) {
            SettingScreen(navController = navController)
        }
    }
}