import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.google.accompanist.permissions.*
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import com.grayseal.forecastapp.data.DataOrException
import com.grayseal.forecastapp.location.CustomDialog
import com.grayseal.forecastapp.model.Weather
import com.grayseal.forecastapp.screens.main.MainViewModel

@ExperimentalPermissionsApi
@Composable
fun GetCurrentLocation(
    mainViewModel: MainViewModel,
    context: Context,
    latitude: MutableState<Double>,
    longitude: MutableState<Double>,
) {
    val permissionState = rememberPermissionState(Manifest.permission.ACCESS_FINE_LOCATION)

    // To create an instance of the fused Location Provider Client
    val fusedLocationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)

    HandleRequest(
        permissionState = permissionState,
        deniedContent = { shouldShowRationale ->
            PermissionDeniedContent(
                rationaleMessage = "We apologize for the inconvenience, but unfortunately this " +
                        "permission is required to use the app",
                shouldShowRationale = shouldShowRationale
            ) { permissionState.launchPermissionRequest() }
        },
        content = {
            // Check to see if permission is available
            if (ContextCompat.checkSelfPermission(
                    context,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(
                    context,
                    android.Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                try {
                    val locationResult = fusedLocationClient.getCurrentLocation(
                        Priority.PRIORITY_HIGH_ACCURACY,
                        CancellationTokenSource().token
                    )

                    locationResult.addOnSuccessListener { location: Location? ->
                        // Get location. In some rare situations this can be null.
                        if (location != null) {
                            latitude.value = location.latitude
                            longitude.value = location.longitude
                        }
                    }
                } catch (e: Exception) {
                    Toast.makeText(context, "Error Fetching Location", Toast.LENGTH_LONG).show()
                }
            }
            ShowData(
                mainViewModel = mainViewModel,
                latitude = latitude.value,
                longitude = longitude.value
            )
            /*Content(
                   showButton = false
               ) {}*/
        }
    )
}

@ExperimentalPermissionsApi
@Composable
fun HandleRequest(
    permissionState: PermissionState,
    deniedContent: @Composable (Boolean) -> Unit,
    content: @Composable () -> Unit
) {
    when (permissionState.status) {
        is PermissionStatus.Granted -> {
            content()
        }
        is PermissionStatus.Denied -> {
            deniedContent(permissionState.status.shouldShowRationale)
        }
    }
}

@Composable
fun Content(showButton: Boolean = true, onClick: () -> Unit) {
    if (showButton) {
        val enableLocation = remember { mutableStateOf(true) }
        if (enableLocation.value) {
            CustomDialog(
                title = "Turn On Location Service",
                desc = "We understand that privacy is important and we only request access to " +
                        "your location for the purpose of providing you with accurate and relevant " +
                        "weather information.\n\n" +
                        "Unfortunately, without this permission you will not be able to utilize " +
                        "the app's functionalities.",
                enableLocation,
                onClick
            )
        }
    }
}

@ExperimentalPermissionsApi
@Composable
fun PermissionDeniedContent(
    rationaleMessage: String,
    shouldShowRationale: Boolean,
    onRequestPermission: () -> Unit
) {

    if (shouldShowRationale) {

        AlertDialog(
            onDismissRequest = {},
            title = {
                Text(
                    text = "Location Permission Request",
                    textAlign = TextAlign.Center,
                    style = androidx.compose.material3.MaterialTheme.typography.titleLarge,
                    color = Color(0xFF23224a),
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Text(
                    rationaleMessage,
                    modifier = Modifier
                        .padding(top = 10.dp)
                        .fillMaxWidth(),
                    letterSpacing = 0.5.sp,
                    style = androidx.compose.material3.MaterialTheme.typography.bodyLarge,
                    color = Color(0xFF23224a), textAlign = TextAlign.Justify
                )
            },
            confirmButton = {
                androidx.compose.material3.Button(
                    onClick = onRequestPermission,
                    modifier = Modifier
                        .padding(bottom = 10.dp)
                        .background(
                            brush = Brush.horizontalGradient(
                                colors = listOf(
                                    Color(0xFF0b123a),
                                    Color(0xFF2596be)
                                )
                            ),
                            shape = RoundedCornerShape(16.dp)
                        ),
                    colors = androidx.compose.material3.ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                ) {
                    Text("Give Permission", color = Color.White)
                }
            },
        )

    } else {
        Content(onClick = onRequestPermission)
    }
}

@Composable
fun ShowData(mainViewModel: MainViewModel, latitude: Double, longitude: Double) {

    if (latitude != 360.0 && longitude != 360.0) {
        // Latitude and longitude are valid, so continue as normal
        val weatherData = produceState<DataOrException<Weather, Boolean, Exception>>(
            initialValue = DataOrException(loading = true)
        ) {
            Log.d("Lat $ Lon", "$latitude and $longitude")
            value = mainViewModel.getWeatherData(latitude, longitude)
        }.value


        if (weatherData.loading == true) {
            CircularProgressIndicator(color = Color(0xFFd68118))
            androidx.compose.material3.Text("Loading weather information", color = Color.White)
        } else if (weatherData.data != null) {
            androidx.compose.material3.Text(
                text = weatherData.data!!.current.weather[0].description,
                color = Color.White
            )
        }
    } else {
        // Latitude and longitude are not valid, show empty mainScreen
        CircularProgressIndicator(color = Color(0xFFd68118))
        androidx.compose.material3.Text("Retrieving your location", color = Color.White)
    }
}