import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.net.Uri
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.material.Text
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.core.content.ContextCompat
import com.google.accompanist.permissions.*
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.gson.Gson
import com.grayseal.forecastapp.BuildConfig
import com.grayseal.forecastapp.R
import com.grayseal.forecastapp.data.DataOrException
import com.grayseal.forecastapp.location.CustomDialog
import com.grayseal.forecastapp.model.CurrentWeatherObject
import com.grayseal.forecastapp.model.Weather
import com.grayseal.forecastapp.screens.forecast.ForecastViewModel
import com.grayseal.forecastapp.screens.main.MainViewModel
import com.grayseal.forecastapp.ui.theme.poppinsFamily
import com.grayseal.forecastapp.utils.RequestLocationPermission
import com.grayseal.forecastapp.utils.createLocationRequest
import com.grayseal.forecastapp.utils.fetchLastLocation
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.*

@ExperimentalPermissionsApi
@Composable
fun GetCurrentLocation(
    mainViewModel: MainViewModel,
    forecastViewModel: ForecastViewModel,
    context: Context,
    latitude: MutableState<Double>,
    longitude: MutableState<Double>,
) {
    val fusedLocationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)


    var locationFromGps: Location? by remember { mutableStateOf(null) }
    var openDialog: String by remember { mutableStateOf("") }

    val locationPermissionsState = rememberMultiplePermissionsState(
        listOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
        )
    )

    val context = LocalContext.current
    val fusedLocationProviderClient = remember { LocationServices.getFusedLocationProviderClient(context) }
    val locationCallback = remember {
        object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                Log.d("onLocationResult", "locationResult.latitude: ${locationResult.lastLocation?.latitude}")
                locationFromGps = locationResult.lastLocation
            }
        }
    }


    val settingsLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartIntentSenderForResult(),
        onResult = {
            when (it.resultCode) {
                Activity.RESULT_OK -> {
                    context.fetchLastLocation(
                        fusedLocationClient = fusedLocationProviderClient,
                        settingsLauncher = null,
                        location = {
                            Log.d("settingsLauncher", "location: ${it.latitude}")
                            if (locationFromGps == null && locationFromGps != it) {
                                locationFromGps = it
                            }
                        },
                        locationCallback = locationCallback
                    )
                }
                Activity.RESULT_CANCELED -> {
                    Toast.makeText(context, "Activity.RESULT_CANCELED", Toast.LENGTH_LONG).show()
                }
            }
        }
    )

    LaunchedEffect(
        key1 = locationPermissionsState.revokedPermissions.size,
        key2 = locationPermissionsState.shouldShowRationale,
        block = {
            fetchLocation(
                locationPermissionsState,
                context,
                settingsLauncher,
                fusedLocationProviderClient,
                locationCallback,
                openDialog = {
                    openDialog = it
                })
        })

    LaunchedEffect(
        key1 = locationFromGps,
        block = {
            Log.d("LaunchedEffect", "locationFromGps: $locationFromGps")
            // TODO: setup GeoCoder

        }
    )

    DisposableEffect(
        key1 = true
    ) {
        onDispose {
            fusedLocationProviderClient.removeLocationUpdates(locationCallback)
        }
    }

    RequestLocationPermission(permission = Manifest.permission.ACCESS_FINE_LOCATION){

        if (
            locationPermissionsState.revokedPermissions.size == 2
            && !locationPermissionsState.shouldShowRationale
        ) {
            openDialog = "Permission fully denied. Go to settings to enable"
            Log.d("LaunchedEffect", "revokedPermissions.size == 2 && shouldShowRationale")
        } else {
            fetchLocation(
                locationPermissionsState,
                context,
                settingsLauncher,
                fusedLocationProviderClient,
                locationCallback,
                openDialog = {
                    openDialog = it
                }
            )
        }
        locationFromGps?.latitude?.let {
            locationFromGps?.longitude?.let { it1 ->
                ShowData(
                    mainViewModel = mainViewModel,
                    forecastViewModel = forecastViewModel,
                    latitude = it,
                    longitude = it1
                )
            }
        }

        if (openDialog.isNotEmpty()) {
            Dialog(
                onDismissRequest = { openDialog = "" },
                properties = DialogProperties(dismissOnBackPress = false, dismissOnClickOutside = false)
            ) {
                NoPermissionDialog(
                    message = openDialog,
                    reqPermission = {
                        locationPermissionsState.launchMultiplePermissionRequest()
                        openDialog = ""
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalPermissionsApi::class)
private fun fetchLocation(
    locationPermissionsState: MultiplePermissionsState,
    context: Context,
    settingsLauncher: ManagedActivityResultLauncher<IntentSenderRequest, ActivityResult>,
    fusedLocationProviderClient: FusedLocationProviderClient,
    locationCallback: LocationCallback,
    openDialog: (String) -> Unit
) {
    when {
        locationPermissionsState.revokedPermissions.size <= 1 -> {
            // Has permission at least one permission [coarse or fine]
            context.createLocationRequest(
                settingsLauncher = settingsLauncher,
                fusedLocationClient = fusedLocationProviderClient,
                locationCallback = locationCallback
            )
            Log.d("LaunchedEffect", "revokedPermissions.size <= 1")
        }
        locationPermissionsState.shouldShowRationale -> {
            openDialog("Should show rationale")
            Log.d("LaunchedEffect", "shouldShowRationale")
        }
        locationPermissionsState.revokedPermissions.size == 2 -> {
            locationPermissionsState.launchMultiplePermissionRequest()
            Log.d("LaunchedEffect", "revokedPermissions.size == 2")
        }
        else -> {
            openDialog("This app requires location permission")
            Log.d("LaunchedEffect", "else")
        }
    }
}

@Composable
fun NoPermissionDialog(reqPermission: () -> Unit, message: String) {
    Card(
        shape = RoundedCornerShape(10.dp),
        modifier = Modifier.padding(10.dp, 5.dp, 10.dp, 10.dp),
        elevation = 8.dp
    ) {
        val context = LocalContext.current

        Column(
            Modifier.padding(16.dp)
        ) {
            Text(text = message)
            Spacer(modifier = Modifier.height(24.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.align(Alignment.End)) {

                Button(onClick = {
                    if (message == "Permission fully denied. Go to settings to enable") {
                        try {
                            context.startActivity(
                                Intent(
                                    Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                    Uri.parse("package:" + BuildConfig.APPLICATION_ID)
                                )
                            )
                        } catch (e: Exception) {
                            Log.e("NoPermissionDialog", "e:: $e")
                        }
                    } else {
                        reqPermission()
                    }
                }) {
                    Text(text = if (message == "Permission fully denied. Go to settings to enable") "Go to settings" else "Allow")
                }

            }
        }
    }
}

@Composable
fun ShowData(
    mainViewModel: MainViewModel,
    forecastViewModel: ForecastViewModel,
    latitude: Double,
    longitude: Double
) {
    val gson = Gson()
    if (latitude != 360.0 && longitude != 360.0) {
        // Latitude and longitude are valid, so continue as normal
        val weatherData = produceState<DataOrException<Weather, Boolean, Exception>>(
            initialValue = DataOrException(loading = true)
        ) {
            Log.d("Lat $ Lon", "$latitude and $longitude")
            value = mainViewModel.getWeatherData(latitude, longitude)
        }.value


        if (weatherData.loading == true) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CircularProgressIndicator(color = Color(0xFFd68118))
                Spacer(modifier = Modifier.height(10.dp))
                androidx.compose.material3.Text(
                    "Loading weather information",
                    color = Color.White,
                    fontFamily = poppinsFamily
                )
            }
        } else if (weatherData.data != null) {
            forecastViewModel.insertCurrentWeatherObject(
                CurrentWeatherObject(
                    id = 1,
                    gson.toJson(weatherData.data!!)
                )
            )
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                val icon = weatherData.data!!.current.weather[0].icon
                var image = R.drawable.sun_cloudy
                if (icon == "01d") {
                    image = R.drawable.sunny
                } else if (icon == "02d") {
                    image = R.drawable.sunny
                } else if (icon == "03d" || icon == "04d" || icon == "04n" || icon == "03n" || icon == "02n") {
                    image = R.drawable.cloudy
                } else if (icon == "09d" || icon == "10n" || icon == "09n") {
                    image = R.drawable.rainy
                } else if (icon == "10d") {
                    image = R.drawable.rainy_sunny
                } else if (icon == "11d" || icon == "11n") {
                    image = R.drawable.thunder_lightning
                } else if (icon == "13d" || icon == "13n") {
                    image = R.drawable.snow
                } else if (icon == "50d" || icon == "50n") {
                    image = R.drawable.fog
                } else if (icon == "01n") {
                    image = R.drawable.clear
                } else {
                    R.drawable.cloudy
                }
                Box(
                    modifier = Modifier.background(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                colors.secondary,
                                colors.primary, Color.Transparent,
                            ), tileMode = TileMode.Clamp
                        ), alpha = 0.7F
                    )
                ) {
                    Image(
                        painter = painterResource(id = image),
                        contentDescription = "WeatherIcon",
                        modifier = Modifier
                            .scale(
                                1F
                            )
                    )

                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 5.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        weatherData.data!!.current.weather[0].description.split(' ')
                            .joinToString(separator = " ") { word -> word.replaceFirstChar { it.uppercase() } },
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Medium,
                        fontFamily = poppinsFamily
                    )
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        buildAnnotatedString {
                            withStyle(
                                style = SpanStyle(
                                    color = colors.onPrimary,
                                    fontSize = 50.sp,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = poppinsFamily
                                )
                            ) {
                                append(weatherData.data!!.current.temp.toInt().toString())
                            }
                            withStyle(
                                style = SpanStyle(
                                    color = Color(0xFFd68118),
                                    fontSize = 45.sp,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = poppinsFamily
                                )
                            ) {
                                append("°")
                            }
                        })
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 15.dp, end = 15.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(
                        verticalArrangement = Arrangement.SpaceBetween,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.pressure),
                            contentDescription = "PressureIcon",
                            modifier = Modifier
                                .scale(
                                    1F
                                )
                                .size(80.dp)
                        )
                        Text(
                            weatherData.data!!.current.pressure.toString() + "hPa",
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            fontFamily = poppinsFamily
                        )
                        Spacer(modifier = Modifier.height(5.dp))
                        Text("Pressure", fontSize = 12.sp, fontFamily = poppinsFamily)
                    }
                    Column(
                        verticalArrangement = Arrangement.SpaceBetween,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        Image(
                            painter = painterResource(id = R.drawable.wind),
                            contentDescription = "WindIcon",
                            modifier = Modifier
                                .scale(
                                    1F
                                )
                                .size(80.dp)

                        )
                        Text(
                            weatherData.data!!.current.wind_speed.toInt().toString() + "m/s",
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            fontFamily = poppinsFamily
                        )
                        Spacer(modifier = Modifier.height(5.dp))
                        Text("Wind", fontSize = 12.sp, fontFamily = poppinsFamily)
                    }
                    Column(
                        verticalArrangement = Arrangement.SpaceBetween,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.humidity),
                            contentDescription = "HumidityIcon",
                            modifier = Modifier
                                .scale(
                                    1F
                                )
                                .size(80.dp)
                        )
                        Text(
                            weatherData.data!!.current.humidity.toString() + "%",
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            fontFamily = poppinsFamily
                        )
                        Spacer(modifier = Modifier.height(5.dp))
                        Text("Humidity", fontSize = 12.sp, fontFamily = poppinsFamily)
                    }
                }
            }

        }
    } else {
        // Latitude and longitude are not valid, show empty mainScreen
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CircularProgressIndicator(color = Color(0xFFd68118))
            Spacer(modifier = Modifier.height(10.dp))
            androidx.compose.material3.Text(
                "Retrieving your location",
                color = Color.White,
                fontFamily = poppinsFamily
            )
        }
    }
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
        var locationName = ""
        if (addresses != null && addresses.size > 0) {
            val address: Address = addresses[0]
            locationName = address.adminArea
        }
        locationName
    }
}
