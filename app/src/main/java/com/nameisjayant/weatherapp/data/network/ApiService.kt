package com.nameisjayant.weatherapp.data.network

import com.nameisjayant.weatherapp.data.Weather
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    companion object {
        const val BASE_URL = "https://api.weatherapi.com/v1/"

    }

    @GET("current.json")
    suspend fun getWeather(
        @Query("q") name: String,
        @Query("key") key:String = "2c9afa8200644a1086374729232602"
    ) : Weather

}