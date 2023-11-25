package app.climaec.wheather.api

import app.climaec.wheather.model.MyCurrentWheather
import app.climaec.wheather.pronostico.MyPronostico
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiServices {
@GET("weather?")
suspend fun CurrenteWether(
    @Query("q") q:String,
    @Query("units") units:String,
    @Query("appid") appid:String
):Response<MyCurrentWheather>

@GET("forecast?")
suspend fun getForecast(
    @Query("q") q:String,
    @Query("unnits") unnits:String,
    @Query("appid") appid:String,
):Response<MyPronostico>

}