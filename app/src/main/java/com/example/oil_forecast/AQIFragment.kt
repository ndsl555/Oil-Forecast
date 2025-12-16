package com.example.oil_forecast

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.oil_forecast.Adapter.LocationAdapter
import com.example.oil_forecast.Extension.launchAndRepeatWithViewLifecycle
import com.example.oil_forecast.ViewModels.AQIViewModel
import com.example.oil_forecast.databinding.FragmentAqiBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class AQIFragment : Fragment() {
    private var _binding: FragmentAqiBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AQIViewModel by viewModel()

    private lateinit var locationAdapter: LocationAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentAqiBinding.inflate(inflater, container, false)
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

        viewModel.fetchAQIs()
    }

    private fun initView() {
//        locationAdapter =
//            LocationAdapter { geocode ->
//                ForecastDialogFragment
//                    .newInstance(geocode)
//                    .show(parentFragmentManager, "forecast_dialog")
//            }
//
//        binding.rvLocations.apply {
//            layoutManager = GridLayoutManager(requireContext(), 3)
//            adapter = locationAdapter
//        }
    }

    private fun initParam() {
        launchAndRepeatWithViewLifecycle {
            viewModel.aqiSortList.collect { locations ->
                binding.title1.text = "NO.1 ${locations.getOrNull(0) ?: "-"}"
                binding.title2.text = "NO.2 ${locations.getOrNull(1) ?: "-"}"
                binding.title3.text = "NO.3 ${locations.getOrNull(2) ?: "-"}"
                binding.title4.text = "NO.1 ${locations.getOrNull(3) ?: "-"}"
                binding.title5.text = "NO.2 ${locations.getOrNull(4) ?: "-"}"
                binding.title6.text = "NO.3 ${locations.getOrNull(5) ?: "-"}"
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
