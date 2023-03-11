package com.grayseal.forecastapp.screens.main

import GetCurrentLocation
import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.grayseal.forecastapp.R
import com.grayseal.forecastapp.screens.forecast.ForecastViewModel
import com.grayseal.forecastapp.ui.theme.poppinsFamily
import com.grayseal.forecastapp.widgets.BottomNavItem
import com.grayseal.forecastapp.widgets.NavBar
import getLocationName
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherScreen(
    navController: NavController,
    mainViewModel: MainViewModel,
    forecastViewModel: ForecastViewModel,
    context: Context,
    city: String?,
) {
    Log.d("City", "$city")
    lateinit var latitude: MutableState<Double>
    lateinit var longitude: MutableState<Double>

    if (city == "default") {
        latitude = remember {
            mutableStateOf(360.0)
        }
        longitude = remember {
            mutableStateOf(360.0)
        }
    }
    else {
        val address = city?.let { getLatLon(context, it) }
        if (address != null) {
            latitude = remember {
                mutableStateOf(address.latitude)
            }
            longitude = remember {
                mutableStateOf(address.longitude)
            }
        }
        else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(start = 15.dp, end = 15.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(R.string.unknown_search),
                    fontFamily = poppinsFamily,
                    fontSize = 16.sp,
                    color = Color.White,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(20.dp))
                TextButton(
                    onClick = {
                        navController.navigate(BottomNavItem.Search.route)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.textButtonColors(containerColor = Color(0xFFd68118)),
                    elevation = ButtonDefaults.buttonElevation(4.dp)
                ) {
                    Text(
                        stringResource(R.string.try_again),
                        fontFamily = poppinsFamily,
                        fontWeight = FontWeight.Medium,
                        color = Color.White
                    )

                }
            }
        }
    }


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
                    .padding(padding)
            ) {
                HomeElements(
                    mainViewModel = mainViewModel,
                    forecastViewModel = forecastViewModel,
                    context = context,
                    latitude = latitude,
                    longitude = longitude,
                )
            }
        }, bottomBar = {
            NavBar(navController)
        }, containerColor = Color.Transparent)
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun HomeElements(
    mainViewModel: MainViewModel,
    forecastViewModel: ForecastViewModel,
    context: Context,
    latitude: MutableState<Double>,
    longitude: MutableState<Double>,
) {

    var locationName by remember {
        mutableStateOf("")
    }
    // cancelled when the composition is disposed
    val scope = rememberCoroutineScope()
    if (latitude.value != 360.0 && longitude.value != 360.0) {
        LaunchedEffect(latitude, longitude) {
            scope.launch {
                locationName = try {
                    getLocationName(context, latitude, longitude)
                } catch (e: Exception) {
                    ""
                }
            }
        }
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 20.dp, bottom = 10.dp), horizontalArrangement = Arrangement.Center
    ) {
        Icon(
            Icons.Outlined.LocationOn,
            contentDescription = stringResource(R.string.location_icon),
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
            stringResource(R.string.today_report),
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = poppinsFamily
        )
    }
    GetCurrentLocation(
        mainViewModel = mainViewModel,
        forecastViewModel = forecastViewModel,
        context = context,
        latitude = latitude,
        longitude = longitude
    )
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

fun getLatLon(context: Context, cityName: String): Address? {
    val geocoder = Geocoder(context)
    return try {
        val addresses = geocoder.getFromLocationName(cityName, 1)
        addresses!![0]
    } catch (e: Exception) {
        Toast.makeText(context, "Unknown Location", Toast.LENGTH_SHORT).show()
        null
    }

}


