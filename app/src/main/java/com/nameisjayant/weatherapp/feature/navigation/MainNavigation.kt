package com.nameisjayant.weatherapp.feature.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.nameisjayant.weatherapp.feature.ui.screens.WeatherDetailScreen
import com.nameisjayant.weatherapp.feature.ui.screens.WeatherScreen


@Composable
fun MainNavigation() {

    val navHostController = rememberNavController()

    NavHost(navController = navHostController, startDestination = WeatherScreen) {
        composable(WeatherScreen) {
            WeatherScreen(navHostController = navHostController)
        }
        composable(WeatherDetailScreen) {
            WeatherDetailScreen(navHostController)
        }
    }
}

const val WeatherScreen = "weather_screen"
const val WeatherDetailScreen = "weather_detail_screen"