package com.headshands.login.view

import android.graphics.Color
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.TextView
import com.headshands.App
import com.headshands.BuildConfig
import com.headshands.service.api.ApiFactory
import com.jakewharton.rxbinding2.widget.RxTextView
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.BiFunction
import io.reactivex.functions.Consumer
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*
import java.util.concurrent.TimeUnit
import com.jakewharton.rxbinding2.view.RxView
import android.R.attr.password
import com.jakewharton.rxbinding2.widget.RxCompoundButton
import android.R.attr.password
import android.text.Editable
import android.text.TextWatcher
import com.headshands.R
import com.jakewharton.rxbinding2.view.clickable


class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.headshands.R.layout.activity_login)
        initializeListeners()
    }

    private fun initializeListeners(){
        editText_email.addTextChangedListener(Textwatcher(editText_email))
        editText_password.addTextChangedListener(Textwatcher(editText_password))

        button_login.setOnClickListener {
            showSnackBar(it)
        }
        button_login.setEnabled(false);
    }

     //               Patterns.EMAIL_ADDRESS.matcher(it).matches() // setting the email pattern - abc@def.com -




    /*
        suspend fun execute(city: City): CityWeather {
        val weather: CurrentWeather? = asyncAwait {
            simulateSlowNetwork()
            weatherRepository.getCurrentWeather(city.cityAndCountry)
        }

        return mapCurrentWeatherToCityWeather(weather, city)
    }
     */
    /*
    private var myJob: Job? = null
    fun runJob(){
        myJob = CoroutineScope(Dispatchers.IO).launch {
            val result = repo.getLeagues()
            withContext(Dispatchers.Main) {
                showSnackBar()
            }
        }
    }
    */
    override fun onResume() {
        super.onResume()

        val weatherService = ApiFactory.weatherApi

        val job = GlobalScope.launch(Dispatchers.Main) {
            val weatherRequest =
                weatherService.getCurrentWeather(
                    App.givenCity.cityAndCountry, App.UNITS, BuildConfig.WEATHER_API_APP_ID
                )
            try {
                val response = weatherRequest.await()

                if(response != null){
                    println(response)
                    Log.d("LoginActivity ","jr")
                }else{
                    Log.d("LoginActivity ","Error")
                }
            }catch (e: java.lang.Exception){

            }
        }


    }

    fun showSnackBar(view: View){
        //Snackbar(view)
        val snackbar = Snackbar.make(view, "Replace with your own action",
            Snackbar.LENGTH_LONG).setAction("Action", null)
        snackbar.setActionTextColor(Color.BLUE)
        val snackbarView = snackbar.view
        snackbarView.setBackgroundColor(Color.LTGRAY)
        val textView =
            snackbarView.findViewById(android.support.design.R.id.snackbar_text) as TextView
        textView.setTextColor(Color.BLUE)
        textView.textSize = 28f
        snackbar.show()
    }

    inner class Textwatcher(var v: View) : TextWatcher {

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) { }

        override fun afterTextChanged(s: Editable?) {

            when (v.getId()) {
                R.id.editText_email -> if (!isValidEmail(editText_email.text.toString().trim())) {
                    wrapper_email.error = "Invalid email format"
                }else{
                    wrapper_email.error = ""
                }

                R.id.editText_password -> if (!isValidPassword(editText_password.text.toString().trim())) {
                    wrapper_password.error = "Password must have at least 6 characters with at least one Capital letter, at least one lower case letter and at least one number"
                }else{
                    wrapper_password.error = ""
                }
            }

            if(isValidEmail(editText_email.text.toString().trim()) && isValidPassword(editText_password.text.toString().trim())){
                button_login.setEnabled(true);
            }else{
                button_login.setEnabled(false);
            }

        }
    }

    private fun isValidEmail(email: String?): Boolean {
         return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun isValidPassword(password: String?) : Boolean {
        val passwordPattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{6,}$"
        val passwordMatcher = Regex(passwordPattern)

        return (passwordMatcher.find(password) != null) true
        else
           false
    }
/*
    fun isValidPassword(password: String?) : Boolean {
        password?.let {
            val passwordPattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{6,}$"
            val passwordMatcher = Regex(passwordPattern)
            return (passwordMatcher.find(password) != null)
        } ?: return false
    }*/

}

/*
myJob = CoroutineScope(Dispatchers.IO).launch {
    val result = repo.getLeagues()
    withContext(Dispatchers.Main) {
        //do something with result
    }
}

private var myJob: Job? = null
override fun onDestroy() {
    myJob?.cancel()
    super.onDestroy()
}

override suspend fun getData(): List<MyData> {
    val result = myService.getData().await()
    return result.map { myDataMapper.mapFromRemote(it) }
}
 */