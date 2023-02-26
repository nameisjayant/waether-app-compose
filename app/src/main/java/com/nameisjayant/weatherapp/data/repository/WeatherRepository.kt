package com.nameisjayant.weatherapp.data.repository

import com.nameisjayant.weatherapp.data.Weather
import com.nameisjayant.weatherapp.data.local.City
import com.nameisjayant.weatherapp.data.local.CityDao
import com.nameisjayant.weatherapp.data.network.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import javax.inject.Inject


class WeatherRepository @Inject constructor(
    private val apiService: ApiService,
    private val dao: CityDao
) {

    suspend fun getWeather(
        city: String
    ): Flow<Weather> = flow {
        emit(apiService.getWeather(city))
    }.flowOn(Dispatchers.IO)

    suspend fun insert(city: City) = withContext(Dispatchers.IO) {
        dao.insert(city)
    }

    fun getAllCities() = dao.getAllCities()


}