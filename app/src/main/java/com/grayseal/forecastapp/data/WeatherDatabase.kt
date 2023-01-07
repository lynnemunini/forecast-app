package com.grayseal.forecastapp.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.grayseal.forecastapp.model.Favourite

@Database(entities = [Favourite::class], version = 1, exportSchema = false)
abstract class WeatherDatabase: RoomDatabase() {
    abstract fun weatherDao(): WeatherDao
}