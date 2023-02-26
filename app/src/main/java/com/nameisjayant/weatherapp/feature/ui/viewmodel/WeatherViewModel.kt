package com.nameisjayant.weatherapp.feature.ui.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nameisjayant.weatherapp.data.Weather
import com.nameisjayant.weatherapp.data.local.City
import com.nameisjayant.weatherapp.data.repository.WeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val repository: WeatherRepository
) : ViewModel() {

    private val _weatherResponse: MutableState<WeatherState> = mutableStateOf(WeatherState())
    val weatherResponse: State<WeatherState> = _weatherResponse

    private val _searchCity: MutableSharedFlow<String> = MutableSharedFlow()

    private val _weatherEventResponse: MutableSharedFlow<WeatherUiEvents<Weather>> = MutableSharedFlow()
    val weatherEventResponse = _weatherEventResponse.asSharedFlow()

    init{
        viewModelScope.launch {
            repository.getAllCities()
                .onStart {
                    _weatherResponse.value = WeatherState(
                        isLoading =  true
                    )
                }.catch {
                    _weatherResponse.value = WeatherState(
                        error = it.message ?: "Something went wrong"
                    )
                }.collect{
                    _weatherResponse.value = WeatherState(
                        data = it
                    )
                }
        }
    }


    fun onEvent(events: WeatherEvents) {
        when (events) {
            is WeatherEvents.SearchWeatherEvent -> {
                viewModelScope.launch {
                    repository.getWeather(events.city)
                        .onStart {
                            _weatherEventResponse.emit(WeatherUiEvents.Loading)
                        }
                        .catch {
                            _weatherEventResponse.emit(
                                WeatherUiEvents.Failure(
                                    it.message ?: "Something went wrong"
                                )
                            )
                        }
                        .collect {
                            _weatherEventResponse.emit(WeatherUiEvents.Success(it))
                        }
                }
            }
            is WeatherEvents.AddCity ->{
                viewModelScope.launch {
                    try{
                        repository.insert(events.city)
                    }catch (e:Exception){

                    }

                }
            }
        }
    }



}

sealed class WeatherEvents {
    data class SearchWeatherEvent(val city: String) : WeatherEvents()
    data class AddCity(val city: City) : WeatherEvents()
}

data class WeatherState(
    val data: List<City> = emptyList(),
    val error: String = "",
    val isLoading: Boolean = false
)

sealed class WeatherUiEvents<out T> {
    data class Success<out T>(val data: Weather) : WeatherUiEvents<T>()
    data class Failure(val msg: String) : WeatherUiEvents<Nothing>()
    object Loading : WeatherUiEvents<Nothing>()
    object Empty : WeatherUiEvents<Nothing>()
}