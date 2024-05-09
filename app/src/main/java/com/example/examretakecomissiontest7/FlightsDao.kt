package com.example.examretakecomissiontest7

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface FlightsDao
{
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(flight: Flight)

    @Delete
    suspend fun delete(flight: Flight)

    @Query("select * from flight")
    fun getAllFlights(): List<Flight>
}