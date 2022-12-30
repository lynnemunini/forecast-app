package com.grayseal.forecastapp.screens.main

import GetCurrentLocation
import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun WeatherScreen(navController: NavController, mainViewModel: MainViewModel, context: Context) {

    val gradientColors = listOf(Color(0xFF060620), colors.primary)
    val latitude = remember {
        mutableStateOf(360.0)
    }
    val longitude = remember {
        mutableStateOf(360.0)
    }
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
            GetCurrentLocation(
                mainViewModel = mainViewModel,
                context = context,
                latitude = latitude,
                longitude = longitude
            )
        }
    }
}



