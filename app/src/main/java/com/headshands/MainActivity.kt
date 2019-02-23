package com.headshands

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import com.jakewharton.rxbinding2.widget.RxTextView
import io.navendra.retrofitkotlindeferred.service.ApiFactory
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        RxTextView.afterTextChangeEvents(editText_email)
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

        RxTextView.afterTextChangeEvents(editText_password)
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
            .subscribe()


    }

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
            val passwordPattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{6,}$"
            val passwordMatcher = Regex(passwordPattern)
            return (passwordMatcher.find(password) != null)
        } ?: return false
    }

    override fun onResume() {
        super.onResume()

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
        }
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