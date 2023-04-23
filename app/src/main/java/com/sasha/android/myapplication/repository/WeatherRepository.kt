package com.sasha.android.myapplication.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import dagger.hilt.android.scopes.ActivityScoped
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@ActivityScoped
object WeatherRepository {

    val stocksListViewState = MutableLiveData<NetworkResponse>()

    fun getWeatherData() {
        val call = RetrofitClient.apiInterface.getWeatherData(lat = "43.6532", lon = "-79.3832")

        call.enqueue(object : Callback<WeatherData> {
            override fun onFailure(call: Call<WeatherData>, t: Throwable) {
                Log.v("DEBUG : ", t.message.toString())

                stocksListViewState.postValue(NetworkResponse.Error(t.localizedMessage ?: "Something went wrong!"))
            }

            override fun onResponse(
                call: Call<WeatherData>,
                response: Response<WeatherData>
            ) {
                Log.v("DEBUG : ", response.body().toString())

                val data = response.body()

                data?.let {
                    stocksListViewState.postValue(NetworkResponse.Success(data))
                } ?: stocksListViewState.postValue(NetworkResponse.Error("Something went wrong!"))
            }
        })
    }
}
