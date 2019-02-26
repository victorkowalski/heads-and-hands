package com.headshands.service.api

import com.headshands.BuildConfig
import com.headshands.forecast.service.api.RetrofitFactory
import com.headshands.forecast.service.api.WeatherApi

object ApiFactory{

    val weatherApi : WeatherApi = RetrofitFactory.retrofit(BuildConfig.WEATHER_API_ENDPOINT)
        .create(WeatherApi::class.java)
}