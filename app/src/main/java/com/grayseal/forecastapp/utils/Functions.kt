package com.grayseal.forecastapp.utils

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.util.Log
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.result.ActivityResult
import androidx.activity.result.IntentSenderRequest
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest

@SuppressLint("MissingPermission", "LongLogTag")
fun Context.createLocationRequest(
    settingsLauncher: ManagedActivityResultLauncher<IntentSenderRequest, ActivityResult>?,
    fusedLocationClient: FusedLocationProviderClient,
    locationCallback: LocationCallback
) {
    val locationRequest = LocationRequest.create().apply {
        interval = 10000
        fastestInterval = 5000
        priority = LocationRequest.PRIORITY_HIGH_ACCURACY
    }

    /* val locationRequest = LocationRequest.create().apply {
         priority = Priority.PRIORITY_HIGH_ACCURACY
         interval = 1 * 1000
         isWaitForAccurateLocation = true

     }*/
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