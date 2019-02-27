package com.headshands.login.view

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.TextView
import com.headshands.App
import com.headshands.BuildConfig
import com.headshands.service.api.ApiFactory
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import android.text.Editable
import android.text.TextWatcher
import android.view.Gravity
import com.headshands.R
import com.headshands.forecast.data.CurrentWeather
import kotlinx.coroutines.Job


class LoginActivity : AppCompatActivity() {

    private var getCurrentWeatherJob: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.headshands.R.layout.activity_login)
        initializeListeners()
    }

    private fun initializeListeners() {
        editText_email.addTextChangedListener(Textwatcher(editText_email))
        editText_password.addTextChangedListener(Textwatcher(editText_password))

        button_login.setOnClickListener {
            showCurrentWeather(root_layout)
        }
        button_login.setEnabled(false);
    }

    private fun showCurrentWeather(view: View) {

        val weatherService = ApiFactory.weatherApi

        getCurrentWeatherJob = GlobalScope.launch(Dispatchers.Main) {
            val weatherRequest =
                weatherService.getCurrentWeather(
                    App.givenCity.cityAndCountry,
                    App.UNITS,
                    BuildConfig.WEATHER_API_APP_ID
                )
            try {
                val response = weatherRequest.await()

                if (response != null) {
                    showSnackBar(view, response)
                    println(response)

                } else {
                    Log.d("LoginActivity ", "WeatherRequest Error")
                }
            } catch (e: java.lang.Exception) {
                Log.d("LoginActivity ", e.message)
            }
        }
    }

    private fun showSnackBar(view: View, currentWeather: CurrentWeather) {

        val snackbar = Snackbar.make(
            view, currentWeather.name + "   " + currentWeather.main!!.temp.toString(),
            Snackbar.LENGTH_LONG
        )
        val snackbarView = snackbar.view
        snackbarView.setBackgroundColor(ContextCompat.getColor(view.context, R.color.colorPrimary))
        val textView =
            snackbarView.findViewById(android.support.design.R.id.snackbar_text) as TextView
        textView.setTextColor(ContextCompat.getColor(view.context, R.color.colorWhite))
        textView.textSize = 28f
        textView.setGravity(Gravity.CENTER_HORIZONTAL);

        snackbar.show()
    }

    private inner class Textwatcher(var v: View) : TextWatcher {

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

        override fun afterTextChanged(s: Editable?) {

            when (v.getId()) {
                R.id.editText_email -> if (!isValidEmail(editText_email.text.toString().trim())) {
                    wrapper_email.error = "Invalid email format"
                } else {
                    wrapper_email.error = ""
                }

                R.id.editText_password -> if (!isValidPassword(editText_password.text.toString().trim())) {
                    wrapper_password.error =
                        "Password must have at least 6 characters with at least one Capital letter, at least one lower case letter and at least one number"
                } else {
                    wrapper_password.error = ""
                }
            }

            if (isValidEmail(editText_email.text.toString().trim()) && isValidPassword(editText_password.text.toString().trim())) {
                button_login.setEnabled(true);
            } else {
                button_login.setEnabled(false);
            }

        }
    }

    private fun isValidEmail(email: String?): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun isValidPassword(password: String): Boolean {

        val passwordPattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{6,}$"
        val passwordMatcher = Regex(passwordPattern)

        if (passwordMatcher.find(password) != null) {
            return true
        }

        return false
    }

    override fun onDestroy() {
        getCurrentWeatherJob?.cancel()
        super.onDestroy()
    }
}