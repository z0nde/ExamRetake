package com.example.examretakecomissiontest7

import com.google.gson.Gson
import org.json.JSONStringer

fun parseJson(jsonString: String): JsonData
{
    return Gson().fromJson(jsonString, JsonData::class.java)
}