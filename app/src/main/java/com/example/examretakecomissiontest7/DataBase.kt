package com.example.examretakecomissiontest7

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [User:: class, Flight::class], version = 1)
abstract class DataBase : RoomDatabase()
{
    abstract val usersDao: UsersDao
    abstract val flightsDao: FlightsDao
}