package com.grayseal.forecastapp.screens.forecast

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.gson.Gson
import com.grayseal.forecastapp.R
import com.grayseal.forecastapp.model.Hourly
import com.grayseal.forecastapp.model.Weather
import com.grayseal.forecastapp.ui.theme.poppinsFamily
import com.grayseal.forecastapp.utils.getCurrentDate
import com.grayseal.forecastapp.widgets.NavBar
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForecastScreen(
    navController: NavController,
    forecastViewModel: ForecastViewModel
) {
    val gson = Gson()
    val currentWeatherObjectsList = forecastViewModel.weatherObjectList.collectAsState().value
    Log.d("CURRENT WEATHER OBJECT LIST", "$currentWeatherObjectsList")
    val currentWeatherObject = currentWeatherObjectsList[0]
    Log.d("WEATHER", currentWeatherObject.weather)
    val weatherData = gson.fromJson(currentWeatherObject.weather, Weather::class.java)

    val gradientColors = listOf(Color(0xFF060620), colors.primary)

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
            Column(
                modifier = Modifier
                    .padding(padding),
            ) {
                ForecastMainElements()
                ForecastData(
                    data = weatherData
                )
            }
        }, bottomBar = {
            NavBar(navController)
        }, containerColor = Color.Transparent)
    }
}

@Composable
fun ForecastMainElements() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 20.dp, bottom = 20.dp),
        horizontalArrangement = Arrangement.Center
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
            .padding(15.dp)
    ) {
        Column(horizontalAlignment = Alignment.Start) {
            Text(
                "Today",
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                fontFamily = poppinsFamily
            )
        }
        Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.End) {
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

    LazyRow(modifier = Modifier.padding(2.dp)) {
        itemsIndexed(items = data.hourly) { index, item: Hourly ->
            val icon = data.hourly[index].weather[0].icon
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
            val instant = Instant.ofEpochSecond(item.dt.toLong())
            val formatter = DateTimeFormatter.ofPattern("HH:mm")
            val time = instant.atZone(ZoneId.of("UTC")).format(formatter)
            HourlyCard(
                image = image,
                time = time,
                temperature = item.temp.toInt().toString()
            )

        }
    }
}

@Composable
fun HourlyCard(image: Int, time: String, temperature: String) {
    Card(
        modifier = Modifier
            .height(100.dp)
            .width(180.dp)
            .padding(top = 20.dp, start = 15.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = colors.secondary),
        elevation = CardDefaults.cardElevation(500.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(0.6f),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(painter = painterResource(id = image), contentDescription = "Weather Icon")

            }
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = time, fontSize = 16.sp, fontFamily = poppinsFamily, color = Color.White)
                Text(
                    text = "$temperature°C",
                    fontSize = 18.sp,
                    fontFamily = poppinsFamily,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

        }
    }
}

@Composable
fun NextForecast(){

}