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
import io.reactivex.Observer
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
import android.widget.Button
import android.widget.EditText
import com.jakewharton.rxbinding2.widget.RxCompoundButton
import io.reactivex.ObservableOnSubscribe
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Function
import io.reactivex.observers.DisposableObserver
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import org.reactivestreams.Subscriber


class LoginActivity : AppCompatActivity() {

    internal lateinit var observable: Observable<Boolean>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.headshands.R.layout.activity_login)
        initialize()

        button_login.setOnClickListener(View.OnClickListener { v ->
            startRStream()
        })
    }

    private fun startRStream() {
//Create an Observable//
        val myObservable = getObservable()
//Create an Observer//
        val myObserver = getObserver()
//Subscribe myObserver to myObservable//
        //myObservable.subscribe(getObserver())
    }
/*
    fun getObserver(): Observer<String> {
        val mySubscriber = object: Observer<String> {
            override fun onNext(s: String) {
                println(s)
            }

            override fun onComplete() {
                println("onComplete")
            }

            override fun onError(e: Throwable) {
                println("onError")
            }

            override fun onSubscribe(s: Disposable) {
                println("onSubscribe")
            }
        }

        return mySubscriber
    }
*/
//Give myObservable some data to emit//

    private fun getObservable(): Observable<String> {
        val observable = Observable
            .just("1" , "2", "3", "4", "5")
            .subscribeOn(Schedulers.newThread())


        return observable
    }

/*
value -> {
            if (value == 1) doMethod2();
            return String.valueOf(value);
        }
 */
private var userNameObservable: Observable<CharSequence>? = null
    private var passwordObservable: Observable<CharSequence>? = null

    /*
    fun test(name: String): Boolean {
        Log.d("test", "test - {it}")
        return true;
    }

    fun test1(name: String): Boolean {
        Log.d("test", "test - {it}")
        return true;
    }
*/
    //internal var et_name: EditText, internal var et_password:EditText
    //internal var btn_login: Button



    fun updateButton(valid: Boolean) {
        if (valid) {
            button_login.setEnabled(true)
        } else {
            button_login.setEnabled(false)
        }
    }

    fun isValidForm(name: String, password: String): Boolean {
        val validName = !name.isEmpty()

        if (!validName) {
            editText_email.setError("Please enter valid name")
        }
        /*&& password.equals(AppPrefs.getPassword()*/
        val validPass = !password.isEmpty()
        if (!validPass) {
            editText_password.setError("Incorrect password")
        }
        return validName && validPass
    }

    private fun getObserver(): Observer<Boolean> {
        return object : Observer<Boolean> {
            override fun onSubscribe(d: Disposable) {
                Log.e("TAG", "onSubscribe: " )
            }

//Every time onNext is called, print the value to Android Studio’s Logcat//

            override fun onNext(s: Boolean) {
                updateButton(s)
            }

//Called if an exception is thrown//

            override fun onError(e: Throwable) {
                Log.e("TAG", "onError: " + e.message)
            }

//When onComplete is called, print the following to Logcat//

            override fun onComplete() {
                Log.d("TAG", "onComplete")
            }
        }
    }

    private fun initialize(){
/*
SubscribeOn specify the Scheduler on which an Observable will operate.
ObserveOn specify the Scheduler on which an observer will observe this Observable.
So basically SubscribeOn is mostly subscribed (executed)
on a background thread ( you do not want to block the UI thread while waiting for the observable)
and also in ObserveOn you want to observe the result on a main thread...
 */
        /*
        val itemInputNameObservable2 = RxTextView.afterTextChangeEvents(editText_email)
            .filter(new Func1<TextViewTextChangeEvent, Boolean>() {
                @Override
                public Boolean call(TextViewTextChangeEvent event) {
                    return event.text().length() >= 3;
                }
            })
        */

        //**************************************************************************************
        val nameObservable = RxTextView.afterTextChangeEvents(editText_email)
            .skip(1)
            .map {
                    charSequence -> charSequence.toString()
            }

        val passwordObservable = RxTextView.afterTextChangeEvents(editText_password)
            .skip(1)
            .map {
                    charSequence -> charSequence.toString()
            }

        observable = Observable.combineLatest(nameObservable, passwordObservable,
            BiFunction {
                   s, s2 -> isValidForm(s, s2)
            })

        observable.subscribe(getObserver())
        //******************************************************************************************

        val name = PublishSubject.create<String>()
        val age = PublishSubject.create<Int>()

        val o1 = Observable.just("Hello World!")
        val o2 = Observable.just("Hel22222222")
/*
        val editText_email = RxTextView.afterTextChangeEvents(editText_email)
            .skipInitialValue()
            .map {
                wrapper_password.error = null
                it.view().text.toString()
            }
        val passwordObservable = RxTextView.afterTextChangeEvents(editText_password)
            .skipInitialValue()
            .map {
                wrapper_password.error = null
                it.view().text.toString()
            }

        Observable.combineLatest<String, String, Boolean>(
            editText_email, passwordObservable,
            BiFunction { n, a ->(test(n) == true && test1(a)==true)}

        )
            .subscribe({
                Log.d("combineLatest", "onNext - ${it}")
                button_login.isEnabled = it
            })

            */


// Can not omit Type parameters and BiFunction
        /* Goodddddd
        Observable.combineLatest<String, String, Boolean>(
            editText_email, passwordObservable,
            BiFunction { n, a ->(n.equals("a"))}

        )
            .subscribe({
                Log.d("combineLatest", "onNext - ${it}")
            })
        */

// If you introduce RxKotlin then you can use type inference
        /*
        Observable.combineLatest(name, age) { n, a -> "$n - age:${a}" }
            .subscribe({
                Log.d("combineLatest", "onNext - ${it}")
            })*/


/*

        val obs = Observable.fromArray(1,2,3,4,5)
        val o1 = Observable.just("Hello World!")

        val x=obs.subscribeOn(Schedulers.io())*/
/*
        Observable.just("long", "longer", "longest")
            .doOnNext { c -> println("processing item on thread " + Thread.currentThread().name) }
            .subscribeOn(Schedulers.newThread())
            .map<Int>({ it.length })
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { length -> println("item length " + length + " received on thread " + Thread.currentThread().name) }
*/



        //**************************************************************************

        /*
        val emailObservable = RxTextView.afterTextChangeEvents(editText_email)
            .skipInitialValue()
            .map {
                wrapper_email.error = null
                it.view().text.toString()
            }
            .debounce(1, TimeUnit.SECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .compose(verifyEmailPattern)
            .compose(retryWhenError {
                wrapper_email.error = it.message
            })
            //.subscribe()

        val passwordObservable = RxTextView.afterTextChangeEvents(editText_password)
            .skipInitialValue()
            .map {
                wrapper_password.error = null
                it.view().text.toString()
            }
            .debounce(1, TimeUnit.SECONDS).observeOn(AndroidSchedulers.mainThread())
            .compose(verifyPasswordLength)
            .compose(retryWhenError {
                wrapper_password.error = it.message
            })
            //.subscribe()

        Observable.combineLatest(arrayOf(emailObservable, passwordObservable)) {
            if(true) {
                println("emailObservable --- "+emailObservable)
                println("passwordObservable --- "+passwordObservable)
            }
        }.subscribe() {
            println(it)
        }
        */

/*
        val isSignInEnabled: Observable<Boolean> = Observable.combineLatest(
            emailObservable,
            passwordObservable,
            BiFunction { u, p -> u == "true" && p == "true" })
*/

        fun test(args: Array<String>) {
            val first = Observable.intervalRange(1, 10, 0, 2, TimeUnit.SECONDS)
            val second = Observable.intervalRange(10, 20, 0, 1, TimeUnit.SECONDS)

            Observable.combineLatest(arrayOf(first, second)) {
                "${it[0]} -> ${it[1]}"
            }.blockingSubscribe() {
                println(it)
            }
        }
//***************************************************************
    /*    val itemInputNameObservable1 = RxTextView.afterTextChangeEvents(editText_email)
            .skipInitialValue()
            .map {
                wrapper_email.error = null
                it.view().text.toString()
            }
  */              /*
                .skipInitialValue()
            .map {
                wrapper_email.error = null
                it.view().text.toString()
            }
            .debounce(1, TimeUnit.SECONDS).observeOn(AndroidSchedulers.mainThread())
            .compose(verifyEmailPattern)
            .compose(retryWhenError {
                wrapper_email.error = it.message
            })
            .subscribe()
                 */
/*
        val itemInputNameObservable2 = RxTextView.afterTextChangeEvents(editText_email)
            .skipInitialValue()
            .map {
                wrapper_email.error = null
                it.view().text.toString()
            }
*/
            /*RxTextView.textChanges(autocomplete_textView)
                .map { inputText: CharSequence -> inputText.isEmpty()
                        || !isValidCityInput(inputText.toString()) }
                .distinctUntilChanged()*/
/*
        val isSignInEnabled: Observable<Boolean> = Observable.combineLatest(
            itemInputNameObservable2,
            itemInputNameObservable1,
            BiFunction { u, p -> u == "true" && p == "true" })
*/

    }


    /*
    private val verifyPasswordLength = ObservableTransformer<String, String> { observable ->
        observable.flatMap {
            Observable.just(it).map { it.trim() } //removing white spaces on the string
                .filter { isValidPassword(it)} //setting the length to 8
                .singleOrError()
                .onErrorResumeNext {
                    if (it is NoSuchElementException) {
                        Single.error(Exception("Password must have at least 6 characters with at least one Capital letter, at least one lower case letter and at least one number")) //informing the user
                    } else {
                        Single.error(it)
                    }
                }
                .toObservable()
        }
    }

    private val verifyEmailPattern = ObservableTransformer<String, String> { observable ->
        observable.flatMap {
            Observable.just(it).map { it.trim() } //removing white spaces
                .filter {
                    Patterns.EMAIL_ADDRESS.matcher(it).matches() // setting the email pattern - abc@def.com -
                }
                .singleOrError()
                .onErrorResumeNext {
                    if (it is NoSuchElementException) {
                        Single.error(Exception("Invalid email format")) //informing the usher
                    } else {
                        Single.error(it)
                    }

                }.toObservable()
        }
    }

    private inline fun retryWhenError(crossinline onError: (ex: Throwable) -> Unit): ObservableTransformer<String, String> = ObservableTransformer { observable ->
        observable.retryWhen { errors ->
            errors.flatMap {
                onError(it)
                Observable.just("")
            }
        }
    }

    fun isValidPassword(password: String?) : Boolean {
        password?.let {
            val passwordPattern = "^(?=.*[0-9])(?=.*[a-z])(?=\\S+$).{6,}$"
            val passwordMatcher = Regex(passwordPattern)
            return (passwordMatcher.find(password) != null)
        } ?: return false
    }

*/
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
  /*
        fun showSnackBar(v: View) {
            val snackbar = Snackbar
                .make(v, "An Error Occurred!", Snackbar.LENGTH_LONG)
                .setAction("RETRY") { }
            snackbar.setActionTextColor(Color.BLUE)
            val snackbarView = snackbar.view
            val textView = snackbarView.findViewById(android.support.design.R.id.snackbar_text)
            textView.setTextColor(Color.RED)
            snackbar.show()
        }
*/
        /*
        CurrentWeather(coord=Coord(lon=-73.99, lat=40.73), weather=[Weather(id=502, main=Rain, description=heavy intensity rain, icon=10n), Weather(id=701, main=Mist, description=mist, icon=50n)], base=stations, main=Main(temp=3.42, tempMin=2.0, tempMax=6.0, pressure=1012.0, seaLevel=null, grndLevel=null, humidity=93, tempKf=null), wind=Wind(speed=4.06, deg=95.5032), clouds=Clouds(all=90), rain=Rain(h=null), dt=1551003300, sys=Sys(type=1, id=5141, message=0.005, country=US, sunrise=1551008224, sunset=1551048103, pod=null), id=5128581, name=New York, cod=200)
         */
       /*
        val shopsService = ApiFactory.shopsApi

        GlobalScope.launch(Dispatchers.Main) {
            val shopListRequest = shopsService.getData()
            try {
                val response = shopListRequest.await()
                if(response.isSuccessful){
                    val shopsResponse = response.body() //This is single object Tmdb Movie response
                    val list  = shopsResponse?.message // This is list of TMDB Movie
                    Log.d("MainActivity ","jr")
                }else{
                    Log.d("MainActivity ",response.errorBody().toString())
                }
            }catch (e: java.lang.Exception){

            }
        }*/
    }
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