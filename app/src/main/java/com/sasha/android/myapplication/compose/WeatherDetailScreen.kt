package com.sasha.android.myapplication

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.PullRefreshState
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.sasha.android.myapplication.compose.LoadingComponent
import com.sasha.android.myapplication.compose.WeatherPreview
import com.sasha.android.myapplication.compose.WeatherPreviewArgs
import com.sasha.android.myapplication.repository.DailyWeather
import com.sasha.android.myapplication.ui.theme.Blue200
import com.sasha.android.myapplication.ui.theme.Blue500
import com.sasha.android.myapplication.ui.theme.Typography
import com.sasha.android.myapplication.viewModels.WeatherDetailViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun WeatherDetailsAdapter(onBackClick: () -> Unit) {
    val viewModel : WeatherDetailViewModel = hiltViewModel()

    var weather by remember {
        mutableStateOf<DailyWeather?>(null)
    }

    val refreshScope = rememberCoroutineScope()
    var refreshing by remember { mutableStateOf(false) }

    fun refresh() = refreshScope.launch {
        viewModel.action.postValue(WeatherDetailViewModel.Action.Refresh)
    }

    val refreshState = rememberPullRefreshState(refreshing, ::refresh)
    viewModel.weather.observeForever {
        weather = it
        refreshing = false
    }

    weather?.let {
        WeatherDetailScreenContainer(
            refreshState = refreshState,
            refreshingState = refreshing,
            onBackClick = onBackClick,
            weather = it
        )
    } ?: LoadingComponent()
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun WeatherDetailScreenContainer(
    refreshState: PullRefreshState,
    refreshingState: Boolean,
    onBackClick: () -> Unit,
    weather: DailyWeather
) {
    Scaffold(
        topBar = {
            TopAppBar(navigationIcon = {
                IconButton(onClick = { onBackClick() }) {
                    Icon(Icons.Default.ArrowBack, "back button", tint = Blue200)
                }
            }, title = {}, backgroundColor = Blue500)
        }, content = {
            Box(Modifier.pullRefresh(refreshState)) {
                Column(
                    Modifier
                        .padding(it)
                        .verticalScroll(rememberScrollState())
                        .fillMaxSize()
                ) {
                    WeatherPreviewDaily(weather)
                    Spacer(modifier = Modifier.padding(dimensionResource(id = R.dimen.small_padding)))
                    WeatherDetails(weather)
                }

                PullRefreshIndicator(
                    refreshing = refreshingState,
                    state = refreshState,
                    modifier = Modifier.align(Alignment.TopCenter)
                )
            }
        })
}

@Composable
fun WeatherPreviewDaily(weather: DailyWeather) {
    WeatherPreview(weatherState = WeatherPreviewArgs(
        temp = weather.temp.day,
        condition = weather.weatherCondition.first(),
        hourlyForecast = listOf(
            WeatherPreviewArgs.Forecast(
                temp = weather.temp.morning,
                condition = weather.weatherCondition[0],
                timeOfDay = "Morning"
            ),
            WeatherPreviewArgs.Forecast(
                temp = weather.temp.day,
                condition = weather.weatherCondition[0],
                timeOfDay = "Day"
            ),
            WeatherPreviewArgs.Forecast(
                temp = weather.temp.evening,
                condition = weather.weatherCondition[0],
                timeOfDay = "Evening"
            ),
            WeatherPreviewArgs.Forecast(
                temp = weather.temp.night,
                condition = weather.weatherCondition[0],
                timeOfDay = "Night"
            )
        )
    ))
}

@Composable
fun WeatherDetails(weather: DailyWeather) {
    Column(
        Modifier
            .fillMaxSize()
            .padding(horizontal = dimensionResource(id = R.dimen.medium_padding))
    ) {
        Text(text = "Current Details", style = Typography.body1)
        Spacer(modifier = Modifier.padding(dimensionResource(id = R.dimen.tiny_padding)))
        TextWithDetailsInRow(title = "Humidity", details = stringResource(id = R.string.percentage_format, weather.humidity))
        TextWithDetailsInRow(title = "Pressure", details = stringResource(id = R.string.pressure_format, weather.pressure))
        weather.visibility?.let {
            TextWithDetailsInRow(title = "Visibility", details = stringResource(id = R.string.distance_format, it))
        }
        TextWithDetailsInRow(title = "Wind", details = stringResource(id = R.string.speed_format, weather.windSpeed.toString()))
    }
}

@Composable
fun TextWithDetailsInRow(title: String, details: String) {
    Row(
        Modifier
            .padding(vertical = dimensionResource(id = R.dimen.small_padding))
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = title, style = Typography.subtitle1)
        Text(text = details, style = Typography.body1)
    }
}

@Preview
@Composable
fun PreviewScreen() {
    WeatherDetailsAdapter({})
}
