package com.grayseal.forecastapp.utils

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

fun getCurrentDate(): String {
    // Get Current Date time in localized style
    val current = LocalDateTime.now()
    val formatter = DateTimeFormatter.ofPattern("EEE, d MMM y", Locale.getDefault())
    return current.format(formatter)
}