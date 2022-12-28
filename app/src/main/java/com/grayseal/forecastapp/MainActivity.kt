package com.grayseal.forecastapp

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.grayseal.forecastapp.navigation.WeatherNavigation
import com.grayseal.forecastapp.ui.theme.ForecastApplicationTheme
import dagger.hilt.android.AndroidEntryPoint

// For dagger to know that this is where we start using and connecting all the dependencies that the project may have
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WeatherApp(context = this)
        }
    }
}

@Composable
fun WeatherApp(context: Context) {
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


//@Preview(showBackground = true)
//@Composable
//fun DefaultPreview(context: Context) {
//    WeatherApp(context = context)
//}