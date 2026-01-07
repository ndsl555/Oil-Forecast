package com.example.oil_forecast.ViewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.oil_forecast.Entity.AQIEntity
import com.example.oil_forecast.UseCase.FetchAQIByLocationUseCase
import com.example.oil_forecast.UseCase.FetchAQIUseCase
import com.example.oil_forecast.Utils.Result
import com.example.oil_forecast.Utils.invoke
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AQIViewModel(
    private val fetchAQIUseCase: FetchAQIUseCase,
    private val fetchAQIByLocationUseCase: FetchAQIByLocationUseCase,
) : ViewModel() {
    private val _currentAqi =
        MutableStateFlow(
            AQIEntity(
                siteName = "",
                county = "",
                aqi = null,
                pollutant = "",
                status = "",
                so2 = null,
                co = null,
                o3 = null,
                o3_8hr = null,
                pm10 = null,
                pm2_5 = null,
                no2 = null,
                nox = null,
                no = null,
                windSpeed = null,
                windDirec = null,
                publishTime = "",
                co_8hr = null,
                pm2_5_avg = null,
                pm10_avg = null,
                so2_avg = null,
                longitude = 0.0,
                latitude = 0.0,
                siteId = "",
            ),
        )
    val currentAqi: StateFlow<AQIEntity> = _currentAqi.asStateFlow()

    //  給列表全覽用
    private val _aqiAllLocations = MutableStateFlow<List<AQIEntity>>(emptyList())
    val aqiAllLocations: StateFlow<List<AQIEntity>> = _aqiAllLocations.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        fetchAQIs()
    }

    fun fetchAQIByLocation(
        lat: Double,
        lng: Double,
    ) {
        viewModelScope.launch {
            val res = fetchAQIByLocationUseCase.invoke(FetchAQIByLocationUseCase.Params(lat, lng))
            when (res) {
                is Result.Success -> {
                    val sortedData = res.data
                    _currentAqi.value = sortedData
                }
                is Result.Error -> {
                    println("AQI fetch error: ${res.exception}")
                }
            }
        }
    }

    fun sortByAscending() {
        _aqiAllLocations.value = _aqiAllLocations.value.sortedBy { it.aqi }
    }

    fun sortByDescending() {
        _aqiAllLocations.value = _aqiAllLocations.value.sortedByDescending { it.aqi }
    }

    fun fetchAQIs() {
        viewModelScope.launch {
            _isLoading.value = true
            val startTime = System.currentTimeMillis()
            try {
                val res = fetchAQIUseCase.invoke()
                when (res) {
                    is Result.Success -> {
                        val sortedData = res.data.sortedBy { it.aqi }
                        _aqiAllLocations.value = sortedData
                    }
                    is Result.Error -> {
                        println("AQI fetch error: ${res.exception}")
                    }
                }
            } finally {
                val elapsedTime = System.currentTimeMillis() - startTime
                val remainingTime = 500L - elapsedTime
                if (remainingTime > 0) {
                    delay(remainingTime)
                }
                _isLoading.value = false
            }
        }
    }
}
