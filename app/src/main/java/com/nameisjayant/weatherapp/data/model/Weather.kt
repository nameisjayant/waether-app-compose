package com.nameisjayant.weatherapp.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Weather(
    val location:Location? = null,
    val current: Current?=null
):Parcelable

@Parcelize
data class Location(
    val name:String?=null,

):Parcelable

@Parcelize
data class Current(
    val temp_c:Double?=null,
    val condition:Condition?=null,
    val feelslike_c:Double?=null,
    val wind_kph:Double?=null

):Parcelable
@Parcelize
data class Condition(
    val text:String?=null
):Parcelable
