package com.sasha.android.myapplication.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.sasha.android.myapplication.repository.DailyWeather
import com.sasha.android.myapplication.repository.NetworkResponse
import com.sasha.android.myapplication.repository.WeatherData
import com.sasha.android.myapplication.repository.WeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import javax.inject.Inject
import kotlin.reflect.KFunction1

@HiltViewModel
class WeatherDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val weatherRepository: WeatherRepository
) : ViewModel() {

    val weather = MutableLiveData<DailyWeather>()
    val action = MutableLiveData<Action>()

    val weatherID : Int = checkNotNull(savedStateHandle["id"])

    init {
        weatherRepository.getWeatherData()

        weatherRepository.stocksListViewState.observeForever {
            when (it) {
                is NetworkResponse.Success -> weather.postValue(it.weatherData.dailyWeather[weatherID])
                else -> {}
            }
        }

        action.observeForever {
            when (it) {
                Action.Refresh -> refresh()
            }
        }
    }

    private fun refresh() {
        weatherRepository.getWeatherData()
    }

    sealed class Action {
        object Refresh : Action()
    }
}

