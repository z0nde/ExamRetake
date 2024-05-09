package com.example.examretakecomissiontest7

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Flight(
    var startCity: String,
    var startCityCode: String,
    var endCity: String,
    var endCityCode: String,
    var startDate: String,
    var endDate: String,
    var price: String,
    @PrimaryKey
    var searchToken: String
)