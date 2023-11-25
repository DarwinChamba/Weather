package app.climaec.wheather.`object`

import app.climaec.wheather.api.ApiServices
import app.climaec.wheather.util.Constants.Companion.BASE_URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ObjectRetrofit {
    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    val api :ApiServices by lazy {
        retrofit.create(ApiServices::class.java)
    }
}