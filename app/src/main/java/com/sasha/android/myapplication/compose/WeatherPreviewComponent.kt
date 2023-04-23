package com.sasha.android.myapplication.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.sasha.android.myapplication.R
import com.sasha.android.myapplication.repository.WeatherCondition
import com.sasha.android.myapplication.toIconRes
import com.sasha.android.myapplication.toTemperature
import com.sasha.android.myapplication.ui.theme.Blue500
import com.sasha.android.myapplication.ui.theme.Blue700
import com.sasha.android.myapplication.ui.theme.Typography
import java.time.LocalDateTime

data class WeatherPreviewArgs(
    val temp: String,
    val condition: WeatherCondition,
    val hourlyForecast: List<Forecast>
) {
    data class Forecast(
        val temp: String,
        val time: LocalDateTime? = null,
        val timeOfDay: String? = null,
        val condition: WeatherCondition? = null,
    )
}

@Composable
fun WeatherPreview(weatherState: WeatherPreviewArgs) {
    val screenHeight = LocalConfiguration.current.screenHeightDp
    Column(
        modifier = Modifier
            .fillMaxSize()
            .defaultMinSize(minHeight = (screenHeight / 2).dp)
            .background(Blue500),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.Start
    ) {
        Row(
            Modifier
                .padding(dimensionResource(id = R.dimen.medium_padding))
                .fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = weatherState.temp.toTemperature(), style = Typography.h2)
            Icon(
                painter = painterResource(id = weatherState.condition.toIconRes()),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(0.5f),
                tint = Color.Unspecified
            )
        }
        Card(
            elevation = 6.dp,
            modifier = Modifier.padding(dimensionResource(id = R.dimen.medium_padding)),
            shape = RoundedCornerShape(20.dp)
        ) {
            LazyRow(
                Modifier
                    .fillMaxWidth()
                    .background(Blue700),
                contentPadding = PaddingValues(horizontal = dimensionResource(id = R.dimen.small_padding), vertical = dimensionResource(id = R.dimen.small_padding))
            ) {
                items(items = weatherState.hourlyForecast, itemContent = {
                    Spacer(modifier = Modifier.padding(dimensionResource(id = R.dimen.small_padding)))
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        it.time?.let {
                            Text(text = it.hour.toString(), style = Typography.body2)
                        } ?: Text(text = it.timeOfDay ?: "", style = Typography.body2)

                        Spacer(modifier = Modifier.padding(dimensionResource(id = R.dimen.tiny_padding)))
                        it.condition?.let {
                            Icon(
                                painterResource(id = it.toIconRes()),
                                contentDescription = it.description,
                                tint = Color.Unspecified,
                                modifier = Modifier.size(dimensionResource(id = R.dimen.icon_size))
                            )
                            Spacer(modifier = Modifier.padding(dimensionResource(id = R.dimen.small_padding)))
                        }

                        Text(text = it.temp.toTemperature(), style = Typography.body2)
                    }
                    Spacer(modifier = Modifier.padding(dimensionResource(id = R.dimen.small_padding)))
                })
            }
        }
    }
}