package com.nameisjayant.weatherapp.feature.ui.screens

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.nameisjayant.weatherapp.data.Weather
import com.nameisjayant.weatherapp.data.local.City
import com.nameisjayant.weatherapp.feature.ui.viewmodel.WeatherEvents
import com.nameisjayant.weatherapp.feature.ui.viewmodel.WeatherUiEvents
import com.nameisjayant.weatherapp.feature.ui.viewmodel.WeatherViewModel
import com.nameisjayant.weatherapp.ui.theme.Background
import com.nameisjayant.weatherapp.ui.theme.GradientColor
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest

@Composable
fun WeatherScreen(
    viewModel: WeatherViewModel = hiltViewModel(),
    navHostController: NavHostController
) {

    var search by remember { mutableStateOf("") }
    val cityResponse = viewModel.weatherResponse.value
    var isLoading by remember { mutableStateOf(false) }
    val context = LocalContext.current


    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 40.dp)
        ) {
            Text(
                text = "Weather", style = TextStyle(
                    color = Color.White,
                    fontSize = 30.sp,
                    fontWeight = FontWeight.SemiBold
                )
            )
            Spacer(modifier = Modifier.height(15.dp))
            AppSearchView(search = search, onValueChange = { search = it }) {
                viewModel.onEvent(WeatherEvents.SearchWeatherEvent(search))
            }
            Spacer(modifier = Modifier.height(15.dp))
            Text(
                text = "History", style = TextStyle(
                    color = Color.White,
                    fontSize = 25.sp,
                    fontWeight = FontWeight.SemiBold
                )
            )
            Spacer(modifier = Modifier.height(15.dp))
            if (cityResponse.data.isNotEmpty())
                LazyColumn {
                    items(cityResponse.data, key = { it.cityName }) {
                        EachRow(data = it)
                    }
                }
            else
                Text(
                    text = "No History Found",
                    modifier = Modifier.padding(10.dp),
                    color = Color.White
                )


        }
    }

    LaunchedEffect(key1 = true) {
        viewModel.weatherEventResponse.collectLatest { response ->
            isLoading = when (response) {
                is WeatherUiEvents.Success -> {
                    val res = response.data
                    viewModel.onEvent(
                        WeatherEvents.AddCity(
                            City(
                                "${res.location?.name}",
                                "${res.current?.temp_c}",
                                "${res.current?.condition?.text}",
                                "${res.current?.feelslike_c}",
                                "${res.current?.wind_kph}"
                            )
                        )
                    )
                    navHostController.currentBackStackEntry?.savedStateHandle?.set("data", res)
                    navHostController.navigate(com.nameisjayant.weatherapp.feature.navigation.WeatherDetailScreen)
                    false
                }
                is WeatherUiEvents.Failure -> {
                    context.showMsg(msg = "No Result found!")
                    false
                }
                WeatherUiEvents.Loading -> {
                    true
                }
                WeatherUiEvents.Empty -> {
                    false
                }
            }
        }
    }

    if (isLoading)
        LoadingDialog()

}

fun Context.showMsg(
    msg: String,
    duration: Int = Toast.LENGTH_SHORT
) {
    Toast.makeText(this, msg, duration).show()
}

@Composable
fun EachRow(
    data: City
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
        backgroundColor = Color.White,
        shape = RoundedCornerShape(16.dp)
    ) {
        Box(
            modifier = Modifier.background(GradientColor)
        ) {
            Column(
                modifier = Modifier.padding(10.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = data.cityName, style = TextStyle(
                            color = Color.White,
                            fontSize = 18.sp
                        ),
                        modifier = Modifier.weight(0.7f)
                    )
                    Text(
                        text = "${data.temp}°C",
                        style = TextStyle(
                            color = Color.White,
                            fontSize = 25.sp
                        ),
                    )
                }
                Text(
                    text = data.wind,
                    style = TextStyle(
                        color = Color.White,
                        fontSize = 16.sp
                    ),
                )
                Spacer(modifier = Modifier.height(20.dp))
                Text(
                    text = data.condition,
                    style = TextStyle(
                        color = Color.White,
                        fontSize = 14.sp
                    ),
                )
            }
        }
    }

}

@Composable
fun HistoryRow(
    data: City,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(5.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(Color.White)
    ) {
        Column(
            modifier = Modifier.padding(15.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = data.cityName,
                    style = TextStyle(
                        color = Color.Black,
                        fontSize = 20.sp
                    ),
                    // modifier = Modifier.weight(0.7f)
                )
                Text(
                    text = data.temp,
                    style = TextStyle(
                        color = Color.Black,
                        fontSize = 25.sp
                    ),
                )
            }
            Spacer(modifier = Modifier.height(5.dp))
            Text(
                text = data.condition, style = TextStyle(
                    color = Color.Black.copy(alpha = 0.5f),
                    fontSize = 14.sp
                ),
                modifier = Modifier.weight(0.6f)
            )
        }
    }

}

@Composable
fun AppSearchView(
    search: String,
    modifier: Modifier = Modifier,
    onValueChange: (String) -> Unit,
    onSearch: () -> Unit
) {

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .fillMaxWidth()
    ) {

        TextField(
            value = search, onValueChange = onValueChange,
            colors = TextFieldDefaults.textFieldColors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                backgroundColor = Color.Gray,
                textColor = Color.White
            ),
            placeholder = {
                Text(text = "Search city", color = Color.White)
            },

            trailingIcon = {
                if (search.isNotEmpty())
                    IconButton(onClick = {
                        onSearch()
                    }) {
                        Icon(Icons.Default.Search, contentDescription = "", tint = Color.White)
                    }
            },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

    }


}

@Composable
fun LoadingDialog() {

    Dialog(onDismissRequest = {}) {
        CircularProgressIndicator()
    }

}