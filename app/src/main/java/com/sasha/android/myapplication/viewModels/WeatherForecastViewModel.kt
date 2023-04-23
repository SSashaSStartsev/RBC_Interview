package com.sasha.android.myapplication.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sasha.android.myapplication.repository.NetworkResponse
import com.sasha.android.myapplication.repository.WeatherData
import com.sasha.android.myapplication.repository.WeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import javax.inject.Inject
import kotlin.reflect.KFunction1

@HiltViewModel
class WeatherForecastViewModel @Inject constructor(
    private val weatherRepository: WeatherRepository,
) : ViewModel() {
    val weather = MutableLiveData<WeatherData>()
    val action = MutableLiveData<Action>()
    val viewActions = MutableLiveData<ViewAction>()

    init {
        weatherRepository.getWeatherData()

        weatherRepository.stocksListViewState.observeForever {
            when (it) {
                is NetworkResponse.Error -> viewActions.postValue(ViewAction.ShowSnackbar(it.errorMessage))
                is NetworkResponse.Success -> weather.postValue(it.weatherData)
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

    sealed class ViewAction {
        data class ShowSnackbar(val message: String): ViewAction()
    }
}

