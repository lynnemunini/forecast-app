package com.grayseal.forecastapp.data

import androidx.room.*
import com.grayseal.forecastapp.model.Favourite
import kotlinx.coroutines.flow.Flow

@Dao
interface WeatherDao {
    // Add all queries to interface with the database
    @Query("SELECT * from favourite_tbl")
    fun getFavourites(): Flow<List<Favourite>>

    @Query("SELECT * from favourite_tbl where city =:city")
    suspend fun getFavById(city: String): Favourite

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavourite(favourite: Favourite)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateFavourite(favourite: Favourite)

    @Query("DELETE from favourite_tbl")
    suspend fun deleteAllFavourites()

    @Delete
    suspend fun deleteFavourite(favourite: Favourite)
}