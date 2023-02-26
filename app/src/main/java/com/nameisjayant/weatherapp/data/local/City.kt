package com.nameisjayant.weatherapp.data.local

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "city")
@Parcelize
data class City(
    @PrimaryKey
    val cityName:String,
    val temp:String,
    val condition:String,
    val feelsLike:String,
    val wind:String
):Parcelable