package com.grayseal.forecastapp

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

//Ties everything together in terms of the dependency injection
@HiltAndroidApp
class WeatherApplication: Application(){
}