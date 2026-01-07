package com.example.oil_forecast.ViewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.oil_forecast.Entity.OilPrice
import com.example.oil_forecast.UseCase.FetchOilPriceUseCase
import com.example.oil_forecast.Utils.Result
import com.example.oil_forecast.Utils.invoke
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class OilPriceViewModel(
    private val fetchOilPriceUseCase: FetchOilPriceUseCase,
) : ViewModel() {
    private val _oilPriceList = MutableStateFlow<List<OilPrice>>(emptyList())
    val oilPriceList: StateFlow<List<OilPrice>> = _oilPriceList.asStateFlow()

    init {
        fetchOilPrice()
    }

    fun fetchOilPrice() {
        viewModelScope.launch {
            try {
                val res = fetchOilPriceUseCase.invoke()
                when (res) {
                    is Result.Success -> {
                        println(res)
                        _oilPriceList.value = res.data
                    }
                    is Result.Error -> {
                        println("oil fetch error: ${res.exception}")
                    }
                }
            } catch (e: Exception) {
                println("AQI fetch error: $e")
            }
        }
    }
}
