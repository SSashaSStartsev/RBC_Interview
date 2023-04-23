package com.sasha.android.myapplication.repository

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

object RetrofitClient {
    val API_KEY = "eac29da07518284e5c5803fad93f29c3"

    val retrofitClient: Retrofit.Builder by lazy {
        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)

        val okhttpClient = OkHttpClient.Builder()
        okhttpClient.addInterceptor(logging)

        Retrofit.Builder()
            .baseUrl("https://api.openweathermap.org/data/3.0/")
            .client(okhttpClient.build())
            .addConverterFactory(JacksonConverterFactory.create())
    }

    val apiInterface: ApiInterface by lazy {
        retrofitClient
            .build()
            .create(ApiInterface::class.java)
    }
}

interface ApiInterface {
    @GET("onecall")
    fun getWeatherData(@Query("lat") lat: String, @Query("lon") lon: String, @Query("exclude") exclude: String = "minutely,alerts", @Query("appid") api_key: String = RetrofitClient.API_KEY, @Query("units") units: String = "metric", @Query("timezone") timezone: String = "America/New_York") : Call<WeatherData>
}