package com.grayseal.forecastapp.widgets

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Analytics
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun NavBar() {
    var selectedItem by remember { mutableStateOf(0) }
    val items = mapOf(
        "Home" to Icons.Outlined.Home,
        "Search" to Icons.Outlined.Search,
        "Forecast" to Icons.Outlined.Analytics,
        "Settings" to Icons.Outlined.Settings
    )
    val keys = listOf("Home", "Search", "Forecast", "Settings")
    NavigationBar(
        modifier = Modifier.fillMaxWidth(),
        containerColor = Color.Transparent,
        contentColor = Color.Transparent
    ) {
        keys.forEachIndexed { index, key ->
            NavigationBarItem(
                icon = { items[key]?.let { Icon(it, contentDescription = key) } },
                selected = selectedItem == index,
                onClick = { selectedItem = index },
                colors = NavigationBarItemDefaults.colors(selectedIconColor = colors.secondary, unselectedIconColor = Color.White, indicatorColor = Color.Transparent)
            )
        }
    }
}