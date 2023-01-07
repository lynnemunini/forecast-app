package com.grayseal.forecastapp

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.grayseal.forecastapp.navigation.WeatherNavigation
import com.grayseal.forecastapp.ui.theme.ForecastApplicationTheme
import com.grayseal.forecastapp.ui.theme.poppinsFamily
import dagger.hilt.android.AndroidEntryPoint

// For dagger to know that this is where we start using and connecting all the dependencies that the project may have
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val connectivityManager =
            this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = connectivityManager.activeNetworkInfo
        val isConnected: Boolean = activeNetwork?.isConnected == true
        if (isConnected) {
            setContent {
                WeatherApp(this)
            }
        } else {
            setContent {
                NoConnection(this)
            }
        }
    }
}

@Composable
fun WeatherApp(context: Context) {
    ForecastApplicationTheme {
        // A surface container using the 'background' color from the theme
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = colors.primary
        ) {
            WeatherNavigation(context)
        }
    }
}

@Composable
fun NoConnection(context: Context) {
    ForecastApplicationTheme {
        // A surface container using the 'background' color from the theme
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = colors.primary
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(start = 15.dp, end = 15.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    "No internet connection detected. Please check your internet connection and try again.",
                    fontFamily = poppinsFamily,
                    fontSize = 16.sp,
                    color = Color.White,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(20.dp))
                androidx.compose.material3.TextButton(
                    onClick = {
                        // Check internet connection and refresh app
                        val connectivityManager =
                            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                        val activeNetwork: NetworkInfo? = connectivityManager.activeNetworkInfo
                        val isConnected: Boolean = activeNetwork?.isConnected == true
                        if (isConnected) {
                            // Update the app's content to display the WeatherApp composable
                            (context as MainActivity).setContent {
                                WeatherApp(context)
                            }
                        } else {
                            // Update the app's content to display the NoConnection composable
                            (context as MainActivity).setContent {
                                NoConnection(context)
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.textButtonColors(containerColor = Color(0xFFd68118)),
                    elevation = ButtonDefaults.buttonElevation(4.dp)
                ) {
                    Text(
                        "Try Again",
                        fontFamily = poppinsFamily,
                        fontWeight = FontWeight.Medium,
                        color = Color.White
                    )

                }

            }
        }
    }
}

/* @Preview(showBackground = true)
@Composable
fun DefaultPreview(context: Context, @PreviewParameter(name = "Text") text: String) {
    WeatherApp(context = context)
} */