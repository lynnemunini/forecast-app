package com.grayseal.forecastapp.screens.search

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.grayseal.forecastapp.components.InputField
import com.grayseal.forecastapp.screens.main.MainViewModel
import com.grayseal.forecastapp.ui.theme.poppinsFamily

@Composable
fun LocationScreen(navController: NavController, mainViewModel: MainViewModel) {

}

@Composable
fun SearchScreenElements(){
    val city = remember{
        mutableStateOf("")
    }
    Row(
        modifier = Modifier
            .fillMaxWidth(), horizontalArrangement = Arrangement.Center
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
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = poppinsFamily
        )
    }
    Row(modifier = Modifier.fillMaxWidth()) {
        InputField(valueState = city, labelId = "Search", enabled = true, isSingleLine = true)
    }


}