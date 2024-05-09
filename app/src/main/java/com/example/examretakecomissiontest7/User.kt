package com.example.examretakecomissiontest7

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User(
    var email: String,
    var password: String
){
    @PrimaryKey(autoGenerate = true)
    var idUser: Int = 0
}