package com.grayseal.forecastapp.screens.search

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Cancel
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.grayseal.forecastapp.R
import com.grayseal.forecastapp.components.InputField
import com.grayseal.forecastapp.data.DataOrException
import com.grayseal.forecastapp.model.Favourite
import com.grayseal.forecastapp.model.Weather
import com.grayseal.forecastapp.navigation.WeatherScreens
import com.grayseal.forecastapp.screens.main.MainViewModel
import com.grayseal.forecastapp.screens.main.getLatLon
import com.grayseal.forecastapp.ui.theme.poppinsFamily
import com.grayseal.forecastapp.widgets.NavBar


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    navController: NavController,
    context: Context,
    favouriteViewModel: FavouriteViewModel,
    mainViewModel: MainViewModel
) {
    val gradientColors = listOf(Color(0xFF060620), MaterialTheme.colors.primary)
    var list = favouriteViewModel.favList.collectAsState().value
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
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 20.dp, bottom = 10.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        "Pick location",
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = poppinsFamily
                    )
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth(), horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        "Find the city that you want to know the detailed weather info at this time ",
                        fontSize = 14.sp,
                        fontFamily = poppinsFamily,
                        textAlign = TextAlign.Center
                    )
                }
                SearchBar() { city ->
                    val connectivityManager =
                        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                    val activeNetwork: NetworkInfo? = connectivityManager.activeNetworkInfo
                    val isConnected: Boolean = activeNetwork?.isConnected == true
                    if (isConnected) {
                        navController.navigate(WeatherScreens.WeatherScreen.name + "/$city")
                        val address = getLatLon(context, city)
                        val latitude = address!!.latitude
                        val longitude = address.longitude
                        // Insert record to database
                        favouriteViewModel.insertFavourite(
                            Favourite(
                                city = city,
                                lat = latitude,
                                lon = longitude
                            )
                        )
                    } else {
                        Toast.makeText(context, "No internet connection!", Toast.LENGTH_LONG).show()
                    }

                }
                Log.d("TAG", "$list")
                list = list.reversed()
                if (list.isNotEmpty()) {
                    if (list.size >= 4) {
                        Row(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(start = 15.dp, end = 15.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(modifier = Modifier.fillMaxWidth(0.5f)) {
                                favCard(
                                    0,
                                    favourite = list[0],
                                    context = context,
                                    navController = navController,
                                    mainViewModel = mainViewModel,
                                    favouriteViewModel = favouriteViewModel
                                )
                                favCard(
                                    1,
                                    favourite = list[1],
                                    context = context,
                                    navController = navController,
                                    mainViewModel = mainViewModel,
                                    favouriteViewModel = favouriteViewModel
                                )

                            }
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(start = 20.dp)
                            ) {
                                favCard(
                                    2,
                                    favourite = list[2],
                                    context = context,
                                    navController = navController,
                                    mainViewModel = mainViewModel,
                                    favouriteViewModel = favouriteViewModel
                                )
                                favCard(
                                    3,
                                    favourite = list[3],
                                    context = context,
                                    navController = navController,
                                    mainViewModel = mainViewModel,
                                    favouriteViewModel = favouriteViewModel
                                )

                            }

                        }
                    } else if (list.size == 3) {
                        Row(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(start = 15.dp, end = 15.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(modifier = Modifier.fillMaxWidth(0.5f)) {
                                favCard(
                                    0,
                                    favourite = list[0],
                                    context = context,
                                    navController = navController,
                                    mainViewModel = mainViewModel,
                                    favouriteViewModel = favouriteViewModel
                                )
                                favCard(
                                    1,
                                    favourite = list[1],
                                    context = context,
                                    navController = navController,
                                    mainViewModel = mainViewModel,
                                    favouriteViewModel = favouriteViewModel
                                )

                            }
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(start = 20.dp)
                            ) {
                                favCard(
                                    2,
                                    favourite = list[2],
                                    context = context,
                                    navController = navController,
                                    mainViewModel = mainViewModel,
                                    favouriteViewModel = favouriteViewModel
                                )
                            }
                        }
                    } else if (list.size == 2) {
                        Row(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(15.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(modifier = Modifier.fillMaxWidth(0.5f)) {
                                favCard(
                                    0,
                                    favourite = list[0],
                                    context = context,
                                    navController = navController,
                                    mainViewModel = mainViewModel,
                                    favouriteViewModel = favouriteViewModel
                                )
                                favCard(
                                    1,
                                    favourite = list[1],
                                    context = context,
                                    navController = navController,
                                    mainViewModel = mainViewModel,
                                    favouriteViewModel = favouriteViewModel
                                )

                            }
                        }
                    } else {
                        Row(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(15.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(modifier = Modifier.fillMaxWidth(0.5f)) {
                                favCard(
                                    0,
                                    favourite = list[0],
                                    context = context,
                                    navController = navController,
                                    mainViewModel = mainViewModel,
                                    favouriteViewModel = favouriteViewModel
                                )
                            }
                        }
                    }
                }

            }
        }, bottomBar = {
            NavBar(navController)
        }, containerColor = Color.Transparent)
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SearchBar(onSearch: (String) -> Unit = {}) {
    val searchState = rememberSaveable {
        mutableStateOf("")
    }
    val keyboardController = LocalSoftwareKeyboardController.current
    val valid = remember(searchState.value) {
        searchState.value.trim().isNotEmpty()
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 15.dp, start = 15.dp, end = 15.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        InputField(
            valueState = searchState,
            labelId = "City name",
            enabled = true,
            isSingleLine = true,
            onAction = KeyboardActions {
                if (!valid) return@KeyboardActions
                onSearch(searchState.value.trim())
                keyboardController?.hide()
                searchState.value = ""
            })
    }
}

@Composable
fun favCard(
    index: Int,
    favourite: Favourite,
    context: Context,
    navController: NavController,
    mainViewModel: MainViewModel,
    favouriteViewModel: FavouriteViewModel
) {
    var color = colors.primaryVariant
    if (index == 0) {
        color = colors.secondary
    }
    Card(
        modifier = Modifier
            .padding(top = 10.dp, bottom = 10.dp)
            .clickable {
                val connectivityManager =
                    context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                val activeNetwork: NetworkInfo? = connectivityManager.activeNetworkInfo
                val isConnected: Boolean = activeNetwork?.isConnected == true
                if (isConnected) {
                    navController.navigate(WeatherScreens.WeatherScreen.name + "/${favourite.city}")
                } else {
                    Toast
                        .makeText(context, "No internet connection!", Toast.LENGTH_LONG)
                        .show()
                }
            },
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(500.dp),
        colors = CardDefaults.cardColors(containerColor = color)
    ) {
        val weatherData = produceState<DataOrException<Weather, Boolean, Exception>>(
            initialValue = DataOrException(loading = true)
        ) {
            value = mainViewModel.getWeatherData(favourite.lat, favourite.lon)
        }.value
        Log.d("Weather", "${weatherData.data}")
        if (weatherData.data != null) {
            Log.d("Weather", "${weatherData.data}")
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(15.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.fillMaxWidth(0.5f)) {
                    Text(
                        weatherData.data!!.current.temp.toInt().toString() + "°",
                        fontFamily = poppinsFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 22.sp,
                    )
                    Text(
                        weatherData.data!!.current.weather[0].description.split(' ')
                            .joinToString(separator = " ") { word -> word.replaceFirstChar { it.uppercase() } },
                        fontSize = 11.sp,
                        fontWeight = FontWeight.ExtraLight,
                        fontFamily = poppinsFamily
                    )
                }
                val icon = weatherData.data!!.current.weather[0].icon
                var image = R.drawable.sun_cloudy
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
                Image(
                    painter = painterResource(id = image),
                    contentDescription = "WeatherIcon",
                    modifier = Modifier
                        .scale(
                            1F
                        )
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(15.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.fillMaxWidth(0.7f)) {
                    Text(favourite.city, fontFamily = poppinsFamily, fontWeight = FontWeight.Medium)
                }
                Column(verticalArrangement = Arrangement.Bottom) {
                    Icon(
                        imageVector = Icons.Outlined.Cancel,
                        contentDescription = "Delete Favourite",
                        tint = Color(0xFFd68118),
                        modifier = Modifier.clickable {
                            favouriteViewModel.deleteFavourite(favourite)
                        })

                }
            }
        }
    }
}
