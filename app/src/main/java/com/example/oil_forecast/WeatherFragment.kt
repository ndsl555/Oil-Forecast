package com.example.oil_forecast

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.oil_forecast.Adapter.LocationAdapter
import com.example.oil_forecast.Extension.launchAndRepeatWithViewLifecycle
import com.example.oil_forecast.ViewModels.WeatherViewModel
import com.example.oil_forecast.databinding.FragmentWeatherBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class WeatherFragment : Fragment() {
    private var _binding: FragmentWeatherBinding? = null
    private val binding get() = _binding!!

    private val viewModel: WeatherViewModel by viewModel()

    private lateinit var locationAdapter: LocationAdapter

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

        Log.d("FragmentCheck", "WeatherFragment loaded")

        initView()
        initParam()

        viewModel.fetchLocations()
    }

    private fun initView() {
        locationAdapter =
            LocationAdapter { geocode ->
                ForecastDialogFragment
                    .newInstance(geocode)
                    .show(parentFragmentManager, "forecast_dialog")
            }

        binding.rvLocations.apply {
            layoutManager = GridLayoutManager(requireContext(), 3)
            adapter = locationAdapter
        }
    }

    private fun initParam() {
        launchAndRepeatWithViewLifecycle {
            viewModel.locations.collect { locations ->
                locationAdapter.submit(locations)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
