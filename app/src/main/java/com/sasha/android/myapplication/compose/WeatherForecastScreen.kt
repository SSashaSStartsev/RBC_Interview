package com.sasha.android.myapplication

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.*
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.PullRefreshState
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.sasha.android.myapplication.compose.LoadingComponent
import com.sasha.android.myapplication.compose.WeatherPreview
import com.sasha.android.myapplication.compose.WeatherPreviewArgs
import com.sasha.android.myapplication.repository.*
import com.sasha.android.myapplication.ui.theme.Typography
import com.sasha.android.myapplication.viewModels.WeatherForecastViewModel
import kotlinx.coroutines.launch
import java.time.format.TextStyle
import java.util.*

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun WeatherForecastAdapter(onNavigateToDetails: (id: Int) -> Unit) {
    val viewModel : WeatherForecastViewModel = hiltViewModel()
    var snackbar by remember {
        mutableStateOf<String?>(null)
    }

    viewModel.viewActions.observeForever {
        when (it) {
            is WeatherForecastViewModel.ViewAction.ShowSnackbar -> snackbar = it.message
        }
    }

    var weather by remember {
        mutableStateOf<WeatherData?>(null)
    }

    val refreshScope = rememberCoroutineScope()
    var refreshing by remember { mutableStateOf(false) }

    fun refresh() = refreshScope.launch {
        viewModel.action.postValue(WeatherForecastViewModel.Action.Refresh)
    }

    val refreshState = rememberPullRefreshState(refreshing, ::refresh)

    viewModel.weather.observeForever {
        refreshing = false
        weather = it
    }

    weather?.let {
        ForecastScreenContainer(
            state = it,
            refreshState = refreshState,
            refreshingState = refreshing,
            onNavigateToDetails = onNavigateToDetails
        )
    } ?: LoadingComponent()
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ForecastScreenContainer(
    state: WeatherData,
    refreshState: PullRefreshState,
    refreshingState: Boolean,
    onNavigateToDetails: (id: Int) -> Unit,
    snackbarMessage: String? = null
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .pullRefresh(refreshState)
    ) {
        WeeklyPreviewList(onNavigateToDetails = onNavigateToDetails, state = state)
        PullRefreshIndicator(
            refreshing = refreshingState,
            state = refreshState,
            modifier = Modifier.align(Alignment.TopCenter)
        )
        snackbarMessage?.let {
            Snackbar {
                Text(text = it)
            }
        }
    }
}

@Composable
fun DailyWeatherPreview(currentWeather: Weather, hourlyWeather: List<Weather>) {
    WeatherPreview(weatherState = WeatherPreviewArgs(
        temp = currentWeather.temp,
        condition = currentWeather.weatherCondition.first(),
        hourlyForecast = hourlyWeather.map { WeatherPreviewArgs.Forecast(
            temp = it.temp,
            time = it.currentTime,
            condition = it.weatherCondition.first()
        ) }.subList(0, 12)
    ))
}

@Composable
fun WeeklyPreviewList(onNavigateToDetails: (id: Int) -> Unit, state: WeatherData) {
    LazyColumn(Modifier.fillMaxSize()) {
        item {
            DailyWeatherPreview(state.currentWeather, state.hourlyWeather)
        }
        itemsIndexed(state.dailyWeather) { index, state ->
            WeeklyForecastLine(onNavigateToDetails = onNavigateToDetails, state = state, index = index)
            Divider()
        }
    }
}

@Composable
fun WeeklyForecastLine(onNavigateToDetails: (id: Int) -> Unit, state: DailyWeather, index: Int) {
    Row(
        Modifier
            .clickable { onNavigateToDetails(index) }
            .fillMaxWidth()
            .padding(horizontal = dimensionResource(id = R.dimen.large_padding), vertical = dimensionResource(id = R.dimen.medium_padding)),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = state.currentTime.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.CANADA), modifier = Modifier.weight(3f))
        Icon(
            painter = painterResource(id = state.weatherCondition.first().toIconRes()), modifier = Modifier
                .size(32.dp)
                .weight(1f), contentDescription = null, tint = Color.Unspecified
        )
        Column(
            Modifier
                .weight(1f)
                .padding(horizontal = dimensionResource(id = R.dimen.small_padding)), horizontalAlignment = Alignment.CenterHorizontally
        ) {
            state.temp.max?.let {
                Text(text = it.toTemperature())
            }
            state.temp.min?.let {
                Text(text = it.toTemperature(), style = Typography.subtitle1)
            }
        }
    }
}

@Composable
fun String.toTemperature() = stringResource(id = R.string.temperature_format, this.split(".")[0])

fun WeatherCondition.toIconRes(): Int = when (iconId[0]) {
    '2', '3', '5' -> R.drawable.ic_rainy
    '6' -> R.drawable.ic_snow
    '7' -> R.drawable.ic_cloudy
    '8' -> if (iconId == "800") R.drawable.ic_sunny else R.drawable.ic_cloudy
    else -> R.drawable.ic_thermometer
}
