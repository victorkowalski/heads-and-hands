package com.headshands

import android.app.Application
import com.headshands.forecast.model.City

class App : Application(){

    companion object {
        lateinit var givenCity: City
    }

    override fun onCreate() {
        super.onCreate()
        givenCity = City("New York", "us")
    }
}