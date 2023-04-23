package com.sasha.android.myapplication.repository

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

sealed class NetworkResponse {
    data class Success(val weatherData: WeatherData): NetworkResponse()
    data class Error(val errorMessage: String): NetworkResponse()
}

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
data class WeatherData(
    @JsonProperty("current")
    val currentWeather: Weather,

    @JsonProperty("daily")
    val dailyWeather: List<DailyWeather>,

    @JsonProperty("hourly")
    val hourlyWeather: List<Weather>
)

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
data class Weather(
    @JsonProperty("dt")
    @JsonDeserialize(using = UnixTimestampDeserializer::class)
    val currentTime: LocalDateTime,
    @JsonProperty("temp")
    val temp: String,
    @JsonProperty("feels_like")
    val feelsLike: String,
    @JsonProperty("humidity")
    val humidity: String,
    @JsonProperty("pressure")
    val pressure: String,
    @JsonProperty("visibility")
    val visibility: String,
    @JsonProperty("wind_speed")
    val windSpeed: Int,
    @JsonProperty("weather")
    val weatherCondition: List<WeatherCondition>,
)

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
data class DailyWeather(
    @JsonProperty("dt")
    @JsonDeserialize(using = UnixTimestampDeserializer::class)
    val currentTime: LocalDateTime,
    @JsonProperty("temp")
    val temp: DailyTemperature,
    @JsonProperty("feels_like")
    val feelsLike: DailyTemperature,
    @JsonProperty("humidity")
    val humidity: String,
    @JsonProperty("pressure")
    val pressure: String,
    @JsonProperty("visibility")
    val visibility: String?,
    @JsonProperty("wind_speed")
    val windSpeed: Int,
    @JsonProperty("weather")
    val weatherCondition: List<WeatherCondition>,
)

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
data class DailyTemperature(
    @JsonProperty("morn")
    val morning: String,
    @JsonProperty("day")
    val day: String,
    @JsonProperty("eve")
    val evening: String,
    @JsonProperty("night")
    val night: String,
    @JsonProperty("min")
    val min: String?,
    @JsonProperty("max")
    val max: String?,
)

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
data class WeatherCondition(
    @JsonProperty("main")
    val description: String,
    @JsonProperty("id")
    val iconId: String
)

class UnixTimestampDeserializer: JsonDeserializer<LocalDateTime>() {
    override fun deserialize(p: JsonParser?, ctxt: DeserializationContext?): LocalDateTime {
        val timestamp = p?.text?.trim()?.toLong()
        timestamp?.let {
            val timestampAsDateString = DateTimeFormatter.ISO_LOCAL_DATE_TIME
                .format(Instant.ofEpochSecond(timestamp).atZone(ZoneId.of("EST")))

            return LocalDateTime.parse(timestampAsDateString)
        } ?: throw (Throwable("Something went wrong"))
    }
}