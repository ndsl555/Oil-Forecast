package com.example.oil_forecast.ViewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.oil_forecast.Entity.AQIEntity
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
) : ViewModel() {
    private val _aqiSortList = MutableStateFlow<List<String>>(emptyList())
    val aqiSortList: StateFlow<List<String>> = _aqiSortList.asStateFlow()

    //  給地圖用
    private val _aqiAllLocations = MutableStateFlow<List<AQIEntity>>(emptyList())
    val aqiAllLocations: StateFlow<List<AQIEntity>> = _aqiAllLocations.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        fetchAQIs()
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
                        val firstPart = sortedData.subList(1, minOf(4, sortedData.size))
                        val lastPart = sortedData.takeLast(3)
                        val combined = firstPart + lastPart.reversed()
                        _aqiSortList.value = combined.map { it.county + "" + it.siteName }
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
