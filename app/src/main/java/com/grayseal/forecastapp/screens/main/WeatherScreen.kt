package com.grayseal.forecastapp.screens.main

import android.Manifest
import android.provider.Settings
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
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
import androidx.core.content.ContextCompat.startActivity
import androidx.navigation.NavController
import com.grayseal.forecastapp.data.DataOrException
import com.grayseal.forecastapp.location.requestLocationPermissions
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
        // Latitude and longitude are not valid, so check if the app has permission to access the device's location
        CircularProgressIndicator()
    }
}

fun showPermissionDeniedDialog(context: Context) {
    val builder = AlertDialog.Builder(context)
    builder.setTitle("Permission Denied")
    builder.setMessage("This app needs access to your location to function properly. Please grant the location permission in the app settings.")
    builder.setPositiveButton("Go to Settings") { _, _ ->
        // Open the app settings
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
            /*to create a Uri object from a string that specifies the package name of an app*/
            Uri.parse("package:$context.packageName"))
        context.startActivity(intent)
    }
    builder.setNegativeButton("Cancel") { _, _ ->
        // Do nothing
    }
    builder.create().show()
}