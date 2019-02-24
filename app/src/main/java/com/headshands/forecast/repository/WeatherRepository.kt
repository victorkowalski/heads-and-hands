
package com.headshands.forecast.data.repository

import com.headshands.forecast.data.CurrentWeather
import com.headshands.forecast.data.WeatherForecast

interface WeatherRepository {
    fun getCurrentWeather(cityAndCountry: String): CurrentWeather?
    fun getWeatherForecast(cityAndCountry: String): WeatherForecast?
}