package com.grayseal.forecastapp.model

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favourite_tbl")
data class Favourite(
    @NonNull
    @PrimaryKey
    @ColumnInfo(name = "city")
    val city: String,

    @ColumnInfo(name = "latitude")
    val lat: Double,

    @ColumnInfo(name = "longitude")
    val lon: Double
)
