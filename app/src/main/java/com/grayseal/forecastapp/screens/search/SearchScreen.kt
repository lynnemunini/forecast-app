package com.grayseal.forecastapp.screens.search

import android.content.Context
import android.location.Address
import android.location.Geocoder
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.grayseal.forecastapp.components.InputField
import com.grayseal.forecastapp.navigation.WeatherScreens
import com.grayseal.forecastapp.ui.theme.poppinsFamily
import com.grayseal.forecastapp.widgets.NavBar


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(navController: NavController) {
    val gradientColors = listOf(Color(0xFF060620), MaterialTheme.colors.primary)
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
                SearchBar(navController = navController) { city ->
                    navController.navigate(WeatherScreens.WeatherScreen.name + "/$city")
                }
            }
        }, bottomBar = {
            NavBar(navController)
        }, containerColor = Color.Transparent)
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SearchBar(navController: NavController, onSearch: (String) -> Unit = {}) {
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
            .padding(15.dp),
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
