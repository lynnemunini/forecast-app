package com.grayseal.forecastapp.screens.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController

@Composable
fun WeatherScreen(navController: NavController){
    val gradientColors = listOf(Color(0xFF060620), colors.primary)
    Box(modifier = Modifier
        .fillMaxSize()
        .background(
        brush = Brush.linearGradient(colors = gradientColors, start = Offset(0f, Float.POSITIVE_INFINITY), end = Offset(Float.POSITIVE_INFINITY, 0f)))){}
}