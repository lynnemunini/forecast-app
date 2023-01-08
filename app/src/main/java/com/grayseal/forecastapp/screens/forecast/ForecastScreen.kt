package com.grayseal.forecastapp.screens.forecast

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.material.Text
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.grayseal.forecastapp.R
import com.grayseal.forecastapp.data.DataOrException
import com.grayseal.forecastapp.model.Hourly
import com.grayseal.forecastapp.model.Weather
import com.grayseal.forecastapp.ui.theme.poppinsFamily
import com.grayseal.forecastapp.utils.getCurrentDate
import com.grayseal.forecastapp.widgets.NavBar

@Composable
fun ForecastScreen(
    navController: NavController,
    forecastViewModel: ForecastViewModel
) {
   /* val gradientColors = listOf(Color(0xFF060620), colors.primary)
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
            if (weatherData.data != null) {
                Column(
                    modifier = Modifier
                        .padding(padding),
                ) {
                    ForecastMainElements()
                    ForecastData(
                        data = weatherData.data!!
                    )
                }
            } else {
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
            }
        }, bottomBar = {
            NavBar(navController)
        }, containerColor = Color.Transparent)
    }*/
}

@Composable
fun ForecastMainElements() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Text(
            "Forecast Report",
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = poppinsFamily
        )
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Column(horizontalAlignment = Alignment.Start) {
            Text(
                "Today",
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                fontFamily = poppinsFamily
            )
        }
        Column(horizontalAlignment = Alignment.End) {
            Text(
                getCurrentDate(),
                fontSize = 14.sp,
                fontWeight = FontWeight.Light,
                fontFamily = poppinsFamily
            )
        }
    }
}

@Composable
fun ForecastData(data: Weather) {
    val icon = data.hourly[0].weather[0].icon
    var image = R.drawable.cloudy
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
    LazyRow(modifier = Modifier.padding(2.dp), contentPadding = PaddingValues(1.dp)) {
        items(items = data.hourly) { item: Hourly ->
            HourlyCard(
                image = image,
                time = java.time.format.DateTimeFormatter.ISO_INSTANT
                    .format(java.time.Instant.ofEpochSecond(item.dt.toLong())),
                temperature = item.temp.toString()
            )

        }
    }
}

@Composable
fun HourlyCard(image: Int, time: String, temperature: String) {
    Card(
        modifier = Modifier
            .height(50.dp)
            .width(70.dp),
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = colors.secondary),
        elevation = CardDefaults.cardElevation(20.dp)
    ) {
        Column(verticalArrangement = Arrangement.Center) {
            Image(painter = painterResource(id = image), contentDescription = "Weather Icon")

        }
        Column() {
            Text(text = time, fontSize = 12.sp, fontFamily = poppinsFamily, color = Color.White)
            Text(
                text = "$temperature°",
                fontSize = 12.sp,
                fontFamily = poppinsFamily,
                color = Color.White
            )

        }
    }
}