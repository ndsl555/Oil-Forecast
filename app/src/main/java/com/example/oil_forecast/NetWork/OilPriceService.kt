package com.example.oil_forecast.NetWork

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET

interface OilPriceService {
    @GET("/")
    suspend fun getOilPrice(): Response<ResponseBody>
}
