package com.grayseal.forecastapp.screens.main

import GetCurrentLocation
import android.content.Context
import android.location.Geocoder
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.grayseal.forecastapp.ui.theme.poppinsFamily
import com.grayseal.forecastapp.widgets.NavBar
import getLocationName
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

@OptIn(ExperimentalPermissionsApi::class, ExperimentalMaterial3Api::class)
@Composable
fun WeatherScreen(navController: NavController, mainViewModel: MainViewModel, context: Context) {
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
        Scaffold(content = { padding ->
            Column(
                modifier = Modifier
                    .padding(padding),
            ) {
                HomeElements(
                    navController = navController,
                    mainViewModel = mainViewModel,
                    context = context
                )
            }
        }, bottomBar = {
            NavBar()
        }, containerColor = Color.Transparent)
    }

}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun HomeElements(navController: NavController, mainViewModel: MainViewModel, context: Context) {

    val latitude = remember {
        mutableStateOf(360.0)
    }
    val longitude = remember {
        mutableStateOf(360.0)
    }
    var locationName by remember {
        mutableStateOf("")
    }
    // cancelled when the composition is disposed
    val scope = rememberCoroutineScope()
    if (latitude.value != 360.0 && longitude.value != 360.0) {
        LaunchedEffect(latitude, longitude) {
            scope.launch {
                locationName = getLocationName(context, latitude, longitude)
            }
        }
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp), horizontalArrangement = Arrangement.Center
    ) {
        Icon(
            Icons.Outlined.LocationOn,
            contentDescription = "Location Icon",
            tint = colors.secondary
        )
        Text(
            locationName,
            fontSize = 16.sp,
            fontFamily = poppinsFamily
        )
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 15.dp, end = 15.dp), horizontalArrangement = Arrangement.Start
    ) {
        Text(
            "Today's Report",
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = poppinsFamily
        )
    }
    GetCurrentLocation(
        mainViewModel = mainViewModel,
        context = context,
        latitude = latitude,
        longitude = longitude
    )
    NavBar()
}

suspend fun getLocationName(
    context: Context,
    latitude: MutableState<Double>,
    longitude: MutableState<Double>
): String {
    // To specify that the geocoding operation should be performed on the IO dispatcher
    return withContext(Dispatchers.IO) {
        /*
        withContext function will automatically suspend the current coroutine and resume it
        when the operation is complete, allowing other operations to be performed in the meantime
         */
        val geocoder = Geocoder(context, Locale.getDefault())
        val addresses = geocoder.getFromLocation(latitude.value, longitude.value, 1)
        var locationName: String = ""
        if (addresses != null && addresses.size > 0) {
            locationName = addresses[0].locality
        }
        locationName
    }
}



