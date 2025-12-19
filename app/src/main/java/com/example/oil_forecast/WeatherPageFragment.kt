package com.example.oil_forecast

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.oil_forecast.Extension.launchAndRepeatWithViewLifecycle
import com.example.oil_forecast.ViewModels.WeatherViewModel
import com.example.oil_forecast.databinding.FragmentWeatherPageBinding
import com.example.oil_forecast.ui.Adapter.ForecastAdapter
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class WeatherPageFragment : Fragment() {
    private var _binding: FragmentWeatherPageBinding? = null
    private val binding get() = _binding!!

    private val viewModel: WeatherViewModel by viewModel()

    private lateinit var forecastAdapter: ForecastAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentWeatherPageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)

        val geocode = requireArguments().getString(ARG_GEOCODE)!!
        val locationName = requireArguments().getString(ARG_LOCATION_NAME)!!

        initView()
        initParam(geocode, locationName)
    }

    private fun initView() {
        forecastAdapter = ForecastAdapter()
        binding.rvForecast.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = forecastAdapter
        }
    }

    private fun initParam(
        geocode: String,
        locationName: String,
    ) {
        binding.tvLocationName.text = locationName
        launchAndRepeatWithViewLifecycle {
            launch {
                binding.progressBar.isVisible = true
                viewModel.fetchForecast(geocode)
                binding.progressBar.isVisible = false
            }

            launch {
                viewModel.forecast.collect { forecasts ->
                    forecastAdapter.submitList(forecasts)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val ARG_GEOCODE = "arg_geocode"
        private const val ARG_LOCATION_NAME = "arg_location_name"

        fun newInstance(
            geocode: String,
            locationName: String,
        ) = WeatherPageFragment().apply {
            arguments =
                bundleOf(
                    ARG_GEOCODE to geocode,
                    ARG_LOCATION_NAME to locationName,
                )
        }
    }
}
