
package com.headshands.forecast.data.repository

import com.headshands.BuildConfig
import com.headshands.forecast.data.CurrentWeather
import com.headshands.forecast.data.WeatherForecast
import io.navendra.retrofitkotlindeferred.service.ApiFactory.weatherApi

class WeatherRepositoryImpl : WeatherRepository {

    companion object {
        private const val UNITS = "metric"
    }

    override fun getCurrentWeather(cityAndCountry: String): CurrentWeather? {
        return weatherApi
                .getCurrentWeather(cityAndCountry,
                    UNITS, BuildConfig.WEATHER_API_APP_ID)
                .execute()
                .body()
    }

    override fun getWeatherForecast(cityAndCountry: String): WeatherForecast? {
        return weatherApi
                .getWeatherForecast(cityAndCountry,
                    UNITS, BuildConfig.WEATHER_API_APP_ID)
                .execute()
                .body()
    }
}