package com.example.oil_forecast.ViewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.oil_forecast.Entity.ForecastEntity
import com.example.oil_forecast.UseCase.FetchForeCastByGeoUseCase
import com.example.oil_forecast.UseCase.FetchLocationUseCase
import com.example.oil_forecast.Utils.Result
import com.example.oil_forecast.Utils.invoke
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class WeatherViewModel(
    private val fetchLocationUseCase: FetchLocationUseCase,
    private val fetchForeCastByGeoUseCase: FetchForeCastByGeoUseCase,
) : ViewModel() {
    private val _locations = MutableStateFlow<List<Pair<String, String>>>(emptyList())
    val locations: StateFlow<List<Pair<String, String>>> = _locations.asStateFlow()

    private val _forecast = MutableStateFlow<List<ForecastEntity>>(emptyList())
    val forecast: StateFlow<List<ForecastEntity>> = _forecast

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    fun fetchLocations() {
        viewModelScope.launch {
            val res = fetchLocationUseCase.invoke()
            when (res) {
                is Result.Success -> {
                    println("長度" + res.data.size)
                    // 轉成只要鄉鎮名稱的 List<String>
                    _locations.value = res.data.map { it.locationName to it.geocode }
                }
                is Result.Error -> {
//                    println()
                    _error.value = res.exception.message ?: "未知錯誤"
                }
            }
        }
    }

    fun fetchForecast(geocode: String) {
        viewModelScope.launch {
            val res = fetchForeCastByGeoUseCase.invoke(FetchForeCastByGeoUseCase.Params(geocode))
            when (res) {
                is Result.Success -> {
                    println("預報${res.data}")
                    _forecast.value = res.data
                }

                is Result.Error -> _error.value = res.exception.message ?: "取得天氣失敗"
            }
        }
    }
}
