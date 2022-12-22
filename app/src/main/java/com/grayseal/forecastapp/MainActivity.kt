package com.grayseal.forecastapp

import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import com.grayseal.forecastapp.location.CustomDialog
import com.grayseal.forecastapp.navigation.WeatherNavigation
import com.grayseal.forecastapp.ui.theme.ForecastApplicationTheme
import dagger.hilt.android.AndroidEntryPoint

// For dagger to know that this is where we start using and connecting all the dependencies that the project may have
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WeatherApp(this)
        }
    }

}

@Composable
fun WeatherApp(context : Context) {
    ForecastApplicationTheme {
        // A surface container using the 'background' color from the theme
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colors.primary
        ) {
            WeatherNavigation(context = context)
        }
    }
}

//
//@Preview(showBackground = true)
//@Composable
//fun DefaultPreview(context: Context) {
//    WeatherApp(context = context)
//}