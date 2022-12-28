package com.grayseal.forecastapp.location

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import android.provider.Settings
import android.util.Log
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
        ) == PackageManager.PERMISSION_DENIED ||
        ContextCompat.checkSelfPermission(
            context,
            ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_DENIED
    ) {
        requestLocationPermissions(context)

        // Check if the user has clicked "Deny" after requesting permissions and show the permissions rationale only in that case

        /* if (ActivityCompat.shouldShowRequestPermissionRationale(
                context as Activity,
                ACCESS_COARSE_LOCATION
            ) ||
            ActivityCompat.shouldShowRequestPermissionRationale(context, ACCESS_FINE_LOCATION)
        ) {
            // The user has previously denied the permission request and selected the "Never ask again" option
            // Show the permissions rationale
            showPermissionDeniedDialog(context)
        } else {
            // The user has either granted the permission or denied the permission without selecting the "Never ask again" option
            // Request the permissions
            requestLocationPermissions(context)
        } */
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
fun requestLocationPermissions(context: Context) {
    ActivityCompat.requestPermissions(
        context as Activity,
        arrayOf(ACCESS_COARSE_LOCATION, ACCESS_FINE_LOCATION),
        1
    )
}

// Permission denied rationale
fun showPermissionDeniedDialog(context: Context) {
    val builder = AlertDialog.Builder(context)
    builder.setTitle("Permission Denied")
    builder.setMessage("This app needs access to your location to function properly. Please grant the location permission in the app settings.")
    builder.setPositiveButton("Go to Settings") { _, _ ->
        // Open the app settings
        val intent = Intent(
            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
            /*to create a Uri object from a string that specifies the package name of an app*/
            Uri.parse("package:com.grayseal.forecastapp"))
        Log.d("Intent", intent.toString())
        context.startActivity(intent)
    }
    builder.setNegativeButton("Cancel") { _, _ ->
        (context as Activity).finish()
    }
    builder.create().show()
}