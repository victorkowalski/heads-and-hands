package io.navendra.retrofitkotlindeferred.service

import com.headshands.BuildConfig
import com.headshands.forecast.data.service.WeatherApi

object ApiFactory{

    val weatherApi : WeatherApi = RetrofitFactory.retrofit(BuildConfig.WEATHER_API_ENDPOINT)
        .create(WeatherApi::class.java)

    //todo: remove
    val shopsApi : ShopsApi = RetrofitFactory.retrofit(ShopsApi.API_BASE_URL)
        .create(ShopsApi::class.java)
}