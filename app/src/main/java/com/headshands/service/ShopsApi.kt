package io.navendra.retrofitkotlindeferred.service

import com.headshands.data.ShopResponse
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.GET

interface ShopsApi{

    companion object {

        val API_BASE_URL = "https://api-dev.jeench.com/"
    }

    @GET("v1/search-items")
    fun getData(): Deferred<Response<ShopResponse>>
}