
package com.headshands.forecast.model

data class City(
        val cityName: String,
        val country: String) {

    val cityAndCountry: String get() = "$cityName,$country"
}