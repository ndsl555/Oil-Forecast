package com.example.oil_forecast.NetWork

import com.example.oil_forecast.Entity.AirQualityResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface AirQualityService {
    @GET("aqx_p_432")
    suspend fun getAirQualityData(
        @Query("api_key") apiKey: String,
        @Query("limit") limit: Int = 1000,
        @Query("sort") sort: String = "ImportDate desc",
        @Query("format") format: String = "JSON",
    ): Response<AirQualityResponse>
}
