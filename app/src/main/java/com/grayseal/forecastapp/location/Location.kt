package com.grayseal.forecastapp.location

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource


fun getCurrentLocation(context: Context, callback: (Location) -> Unit) {

    // To create an instance of the fused Location Provider Client
    val fusedLocationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)

    // Check for the location permissions
    if (ContextCompat.checkSelfPermission(
            context,
            ACCESS_COARSE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED
    ) {
        // Request the permissions
        requestLocationPermissions(context)
    }
    // Get the current location
    try {
        val locationResult = fusedLocationClient.getCurrentLocation(
            Priority.PRIORITY_HIGH_ACCURACY,
            CancellationTokenSource().token
        )

        locationResult.addOnSuccessListener { location: Location? ->
            // Get location. In some rare situations this can be null.
            if (location != null) {
                callback(location) // Call the callback function with the location
            }
        }
    } catch (e: Exception) {
        Toast.makeText(context, "Error Fetching Location", Toast.LENGTH_LONG).show()
    }
}

// Function to request the location permissions
private fun requestLocationPermissions(context: Context) {
    if (ActivityCompat.shouldShowRequestPermissionRationale(
            context as Activity,
            ACCESS_COARSE_LOCATION
        )
    ) {
        // Show a message to the user explaining why the permission is needed
        Toast.makeText(
            context,
            "We only request access to your location in order to provide you with the best possible weather experience.\n\n" +
                    "Without this permission you will have to manually enter your location.",
            Toast.LENGTH_LONG
        ).show()
    }
    // Request the permission
    ActivityCompat.requestPermissions(
        context,
        arrayOf(ACCESS_COARSE_LOCATION, ACCESS_FINE_LOCATION),
        1
    )
}
