package io.navendra.retrofitkotlindeferred.service

object ApiFactory{

    val shopsApi : ShopsApi = RetrofitFactory.myretrofit(ShopsApi.API_BASE_URL)
        .create(ShopsApi::class.java)
}