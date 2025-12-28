package com.example.oil_forecast
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.oil_forecast.Entity.ForecastEntity
import com.example.oil_forecast.Entity.LocationEntity
import com.example.oil_forecast.Extension.launchAndRepeatWithViewLifecycle
import com.example.oil_forecast.ViewModels.WeatherViewModel
import com.example.oil_forecast.databinding.FragmentWeatherBinding
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.time.LocalDate
import java.time.LocalDateTime

class WeatherFragment : Fragment() {
    private var _binding: FragmentWeatherBinding? = null
    private val binding get() = _binding!!

    private val viewModel: WeatherViewModel by viewModel()

    private var locations: List<LocationEntity> = emptyList()
    private var forecasts: List<ForecastEntity> = emptyList()

    // Add a unit to each metric for dynamic axis formatting
    private enum class ChartMetric(val unit: String) {
        TEMPERATURE("°C"),
        HUMIDITY("%"),
        POP("%"),
        UV_EXPOSURE(""), // UV Index is a level, not a value with a unit
        DEW_POINT("°C"),
    }

    private var currentMetric = ChartMetric.TEMPERATURE

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentWeatherBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        Log.d("FragmentCheck", "WeatherFragment loaded")
        initChart()
        initView()
        initObservers()
        viewModel.fetchLocations()
    }

    private fun initView() {
        // Set the default selection
        binding.chipGroup.check(R.id.chip_temp)
        binding.chipGroup.setOnCheckedChangeListener { _, checkedId ->
            currentMetric =
                when (checkedId) {
                    R.id.chip_temp -> ChartMetric.TEMPERATURE
                    R.id.chip_humidity -> ChartMetric.HUMIDITY
                    R.id.chip_pop -> ChartMetric.POP
                    R.id.chip_uv -> ChartMetric.UV_EXPOSURE
                    R.id.chip_dew -> ChartMetric.DEW_POINT
                    else -> ChartMetric.TEMPERATURE
                }
            updateChart()
        }
    }

    private fun initChart() {
        binding.lineChart.apply {
            description.isEnabled = false
            legend.isEnabled = false
            xAxis.position = XAxis.XAxisPosition.BOTTOM
            xAxis.textColor = Color.BLACK
            xAxis.setDrawGridLines(false)
            axisLeft.textColor = Color.BLACK
            axisRight.isEnabled = false
        }
    }

    private fun initObservers() {
        val locationPref = LocationPref(requireContext())
        val currentCity = locationPref.getLocation()
        val cityPrefix = currentCity?.substring(0, 2)

        launchAndRepeatWithViewLifecycle {
            launch {
                viewModel.locations.collect { locations ->
                    this@WeatherFragment.locations = locations
                    activity?.invalidateOptionsMenu() // Re-creates the options menu with the new locations
                    if (locations.isNotEmpty()) {
                        var initialIndex = 0
                        if (cityPrefix != null) {
                            val matchedIndex =
                                locations.indexOfFirst { it.locationName.contains(cityPrefix) }
                            if (matchedIndex != -1) {
                                initialIndex = matchedIndex
                            }
                        }
                        val initialLocation = locations[initialIndex]
                        binding.currentCityText.text = initialLocation.locationName
                        // Fetch forecast for the initial location (either the stored one or the first in the list)
                        viewModel.fetchForecast(initialLocation.geocode)
                    }
                }
            }

            launch {
                viewModel.forecast.collect { forecasts ->
                    val today = LocalDate.now()
                    this@WeatherFragment.forecasts =
                        forecasts.sortedBy { forecast ->
                            val month = forecast.startTime.substring(0, 2).toInt()
                            val day = forecast.startTime.substring(3, 5).toInt()
                            val hour = forecast.startTime.substring(6, 8).toInt()
                            val minute = forecast.startTime.substring(9, 11).toInt()
                            val second = forecast.startTime.substring(12, 14).toInt()

                            var year = today.year
                            val forecastDate = LocalDate.of(year, month, day)

                            if (forecastDate.isBefore(today.minusDays(7))) {
                                year++
                            }
                            LocalDateTime.of(year, month, day, hour, minute, second)
                        }
                    updateChart()
                }
            }
        }
    }

    private fun updateChart() {
        if (forecasts.isNotEmpty()) {
            val entries = getChartData(forecasts, currentMetric)

            // This formatter will add the correct unit to the values
            val unitFormatter =
                object : ValueFormatter() {
                    override fun getFormattedValue(value: Float): String {
                        // For UV, we show a level description instead of a unit
                        if (currentMetric == ChartMetric.UV_EXPOSURE) {
                            return when (value.toInt()) {
                                0, 1, 2 -> "低"
                                3, 4, 5 -> "中"
                                6, 7 -> "高"
                                8, 9, 10 -> "過量"
                                11 -> "危險"
                                else -> ""
                            }
                        }
                        return "${value.toInt()}${currentMetric.unit}"
                    }
                }

            val dataSet =
                LineDataSet(entries, currentMetric.name).apply {
                    color = Color.BLUE
                    valueTextColor = Color.BLACK
                    lineWidth = 2f
                    setDrawCircles(true)
                    setCircleColor(Color.BLUE)
                    // Apply the formatter to the values on the chart
                    valueFormatter = unitFormatter
                }

            // Apply the formatter to the Y-axis
            binding.lineChart.axisLeft.valueFormatter = unitFormatter
            binding.lineChart.data = LineData(dataSet)

            binding.lineChart.xAxis.valueFormatter =
                object : ValueFormatter() {
                    override fun getFormattedValue(value: Float): String {
                        if (value.toInt() >= 0 && value.toInt() < forecasts.size) {
                            val startTime = forecasts[value.toInt()].startTime
                            return startTime.substringBefore('T') // Directly return the date part
                        }
                        return ""
                    }
                }
            binding.lineChart.invalidate() // refresh
        } else {
            // Clear the chart if there is no data
            binding.lineChart.clear()
            binding.lineChart.invalidate()
        }
    }

    private fun getChartData(
        forecasts: List<ForecastEntity>,
        metric: ChartMetric,
    ): List<Entry> {
        return forecasts.mapIndexed { index, forecast ->
            val yValue =
                when (metric) {
                    ChartMetric.TEMPERATURE -> forecast.avgTemp?.toFloat() ?: 0f
                    ChartMetric.HUMIDITY -> forecast.relativeHumidity?.toFloatOrNull() ?: 0f
                    ChartMetric.POP -> forecast.pop?.toFloat() ?: 0f
                    ChartMetric.UV_EXPOSURE -> forecast.uVIndex?.toFloat() ?: 0f
                    ChartMetric.DEW_POINT -> forecast.dewPoint?.toFloat() ?: 0f
                }
            Entry(index.toFloat(), yValue)
        }
    }

    override fun onCreateOptionsMenu(
        menu: Menu,
        inflater: MenuInflater,
    ) {
        super.onCreateOptionsMenu(menu, inflater)
        menu.clear()
        locations.forEachIndexed { index, location ->
            // Use location index as the itemId
            menu.add(Menu.NONE, index, index, location.locationName)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Check if the selected item id is within the bounds of our locations list
        if (item.itemId >= 0 && item.itemId < locations.size) {
            val selectedLocation = locations[item.itemId]
            binding.currentCityText.text = selectedLocation.locationName
            viewModel.fetchForecast(selectedLocation.geocode)
            // Save the selected location for next time
            val locationPref = LocationPref(requireContext())
            locationPref.saveLocation(selectedLocation.locationName)
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
