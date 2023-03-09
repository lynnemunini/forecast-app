package com.grayseal.forecastapp.utils

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import com.grayseal.forecastapp.location.CustomDialog
import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.util.Log
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.result.ActivityResult
import androidx.activity.result.IntentSenderRequest
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*

@ExperimentalPermissionsApi
@Composable
fun RequestLocationPermission(
    permission: String,
    rationaleMessage: String = "To use this app's functionalities, you need to give us the permission.",
    onPermissions:  @Composable () -> Unit,
) {
    val permissionState = rememberPermissionState(permission)

    HandleRequest(
        permissionState = permissionState,
        deniedContent = { shouldShowRationale ->
            PermissionDeniedContent(
                rationaleMessage = rationaleMessage,
                shouldShowRationale = shouldShowRationale
            ) { permissionState.launchPermissionRequest() }
        },
        content = {
          onPermissions()
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
                    style = MaterialTheme.typography.titleLarge,
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
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color(0xFF23224a), textAlign = TextAlign.Justify
                )
            },
            confirmButton = {
                Button(
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
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
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

@SuppressLint("MissingPermission")
fun Context.fetchLastLocation(
    fusedLocationClient: FusedLocationProviderClient,
    settingsLauncher: ManagedActivityResultLauncher<IntentSenderRequest, ActivityResult>?,
    location: (Location) -> Unit,
    locationCallback: LocationCallback
) {
    fusedLocationClient.lastLocation.addOnSuccessListener {
        if (it != null) {
            location(it)
        } else {
            this.createLocationRequest(
                settingsLauncher = settingsLauncher,
                fusedLocationClient = fusedLocationClient,
                locationCallback = locationCallback
            )
        }
    }
}

@SuppressLint("MissingPermission", "LongLogTag")
fun Context.createLocationRequest(
    settingsLauncher: ManagedActivityResultLauncher<IntentSenderRequest, ActivityResult>?,
    fusedLocationClient: FusedLocationProviderClient,
    locationCallback: LocationCallback
) {

    val locationRequest = LocationRequest.create().apply {
        priority = Priority.PRIORITY_HIGH_ACCURACY
        interval = 1 * 1000
        isWaitForAccurateLocation = true

    }
    val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest)
    val client = LocationServices.getSettingsClient(this)
    val task = client.checkLocationSettings(builder.build())

    task.addOnSuccessListener {
        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            mainLooper
        )
    }

    task.addOnFailureListener { exception ->
        Log.e("LocationUtil.createLocationRequest", exception.toString())
        if (exception is ResolvableApiException) {
            try {
                settingsLauncher?.launch(
                    IntentSenderRequest.Builder(exception.resolution).build()
                )
            } catch (e: Exception) {
                // Ignore the error.
            }
        }
    }
}