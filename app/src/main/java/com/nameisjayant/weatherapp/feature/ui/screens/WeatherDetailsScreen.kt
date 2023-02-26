package com.nameisjayant.weatherapp.feature.ui.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.nameisjayant.weatherapp.data.Weather
import com.nameisjayant.weatherapp.data.local.City
import com.nameisjayant.weatherapp.ui.theme.Background
import com.nameisjayant.weatherapp.ui.theme.GradientColor
import com.nameisjayant.weatherapp.ui.theme.GradientTwo


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun WeatherDetailScreen(
    navHostController: NavHostController
) {
    val res = navHostController.previousBackStackEntry?.savedStateHandle?.get<Weather>("data")
        ?: Weather()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(GradientTwo)
    ) {
        Column {
            Spacer(modifier = Modifier.height(20.dp))
            Row(
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 30.dp)
            ) {
                IconButton(onClick = {
                    navHostController.navigateUp()
                }) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "",
                        tint = Color.White
                    )
                }
                Text(
                    text = "${res.location?.name}", style = TextStyle(
                        color = Color.White,
                        fontSize = 35.sp,
                        fontWeight = FontWeight.SemiBold
                    ), modifier = Modifier.padding(start = 10.dp)
                )
            }

            LazyVerticalStaggeredGrid(
                columns = StaggeredGridCells.Fixed(2),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(10.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                item {
                    CityData(title = "Temperature", value = "${res.current?.temp_c}")
                }
                item {
                    CityData(title = "Condition", value = "${res.current?.condition?.text}")
                }
                item {
                    CityData(title = "Feels Like", value = "${res.current?.feelslike_c}")
                }
                item {
                    CityData(title = "Wind Speed", value = "${res.current?.wind_kph}Kp/h")
                }
            }
        }

    }

}

@Composable
fun CityData(
    title: String,
    value: String
) {

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        elevation = 5.dp
    ) {
        Box(
            modifier = Modifier
                .background(Color(0XFF0074C4))
        ) {
            Column(
                modifier = Modifier.padding(20.dp)
            ) {
                Text(
                    text = title, style = TextStyle(
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = value, style = TextStyle(
                        color = Color.White.copy(alpha = 0.4f),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.W500
                    )
                )
            }
        }
    }

}