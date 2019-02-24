package com.headshands.forecast.data.service

import com.headshands.forecast.data.CurrentWeather
import com.headshands.forecast.data.WeatherForecast
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {
    // Docs: https://openweathermap.org/current
    // Example: http://api.openweathermap.org/data/2.5/weather?q=London,uk&units=metric&appid=f04391f2a7b156421675d08ac24dc908
    @GET("data/2.5/weather")
    fun getCurrentWeather(@Query("q") cityName: String,
                          @Query("units") units: String,
                          @Query("appid") appId: String): Call<CurrentWeather>

    // Docs: https://openweathermap.org/forecast5
    // Example: http://api.openweathermap.org/data/2.5/forecast?q=London,uk&units=metric&appid=f04391f2a7b156421675d08ac24dc908
    @GET("data/2.5/forecast")
    fun getWeatherForecast(@Query("q") cityName: String,
                           @Query("units") units: String,
                           @Query("appid") appId: String): Call<WeatherForecast>
}