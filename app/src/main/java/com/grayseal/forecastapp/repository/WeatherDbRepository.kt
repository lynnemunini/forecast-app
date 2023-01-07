package com.grayseal.forecastapp.repository

import com.grayseal.forecastapp.data.WeatherDao
import com.grayseal.forecastapp.model.Favourite
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class WeatherDbRepository @Inject constructor(private val weatherDao: WeatherDao) {

    fun getFavourites(): Flow<List<Favourite>> = weatherDao.getFavourites()
    suspend fun insertFavourite(favourite: Favourite) = weatherDao.insertFavourite(favourite)
    suspend fun updateFavourite(favourite: Favourite) = weatherDao.updateFavourite(favourite)
    suspend fun deleteAllFavourite() = weatherDao.deleteAllFavourites()
    suspend fun deleteFavourite(favourite: Favourite) = weatherDao.deleteFavourite(favourite)
    suspend fun getFavById(city: String): Favourite = weatherDao.getFavById(city)
}