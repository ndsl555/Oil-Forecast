package com.example.oil_forecast.NetWork
import com.example.oil_forecast.Entity.ForecastResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ForeCastService {
    @GET("{id}")
    suspend fun getAllForeCast(
        @Path("id") id: String,
        @Query("Authorization") authKey: String,
    ): Response<ForecastResponse>
}
