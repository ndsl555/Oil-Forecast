package com.example.oil_forecast.ViewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.oil_forecast.Entity.AQIEntity
import com.example.oil_forecast.UseCase.FetchAQIUseCase
import com.example.oil_forecast.Utils.Result
import com.example.oil_forecast.Utils.invoke
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
            val res = fetchAQIUseCase.invoke()
            when (res) {
                is Result.Success -> {
                    _aqiAllLocations.value = res.data
                    val sort = res.data.sortedBy { it.aqi }
                    val firstPart = sort.subList(1, minOf(4, sort.size))
                    val lastPart = sort.takeLast(3)
                    val combined = firstPart + lastPart.reversed()
                    _aqiSortList.value = combined.map { it.county + "" + it.siteName }
                }
                is Result.Error -> {
                    println("AQI fetch error: ${res.exception}")
                }
            }
        }
    }
}
