package com.example.oil_forecast

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.oil_forecast.Extension.launchAndRepeatWithViewLifecycle
import com.example.oil_forecast.ViewModels.AQIViewModel
import com.example.oil_forecast.databinding.FragmentAqiBinding
import com.example.oil_forecast.databinding.ItemPollutionBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class AQIFragment : Fragment() {
    private var _binding: FragmentAqiBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AQIViewModel by viewModel()

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
        val locationPref = LocationPref(requireContext())
        val currentCity = locationPref.getLatLng()
        if (currentCity != null) {
            viewModel.fetchAQIByLocation(currentCity.first, currentCity.second)
        }
        initView()
        initParam()
    }

    private fun initView() {
        binding.btnTest2.setOnClickListener {
            val action = AQIFragmentDirections.actionNavigationAQIFragmentToAqiListFragment()
            findNavController().navigate(action)
        }
    }

    private fun initParam() {
        launchAndRepeatWithViewLifecycle {
            viewModel.currentAqi.collect { aqiEntity ->
                if (aqiEntity.siteName.isNotEmpty()) {
                    bindAQI(aqiEntity.county, aqiEntity.aqi, aqiEntity.status)
                    bindPollution("PM2.5 (μg/m³)", aqiEntity.pm2_5, binding.itemPm25, PollutionType.PM25)
                    bindPollution("PM10 (μg/m³)", aqiEntity.pm10, binding.itemPm10, PollutionType.PM10)
                    bindPollution("O₃ (μg/m³)", aqiEntity.o3, binding.itemO3, PollutionType.O3)
                    bindPollution("NO₂ (μg/m³)", aqiEntity.no2, binding.itemNo2, PollutionType.NO2)
                    bindPollution("CO (μg/m³)", aqiEntity.co, binding.itemCo, PollutionType.CO)
                    bindPollution("SO₂ (μg/m³)", aqiEntity.so2, binding.itemSo2, PollutionType.SO2)
                }
            }
        }
    }

    fun bindAQI(
        siteAndCountry: String,
        aqiValue: Int?,
        status: String?,
    ) {
        val value = aqiValue ?: 0
        val progress = (value * 100 / 500).coerceIn(0, 100)

        val colorRes =
            when {
                value <= 50 -> R.color.aqi_green
                value <= 100 -> R.color.aqi_yellow
                value <= 150 -> R.color.aqi_orange
                value <= 200 -> R.color.aqi_red
                value <= 300 -> R.color.aqi_purple
                else -> R.color.aqi_brown
            }
        binding.textView.text = siteAndCountry
        binding.progressTv.text = "$value"
        binding.statusTv.text = status ?: "--"
        binding.progressTv.setTextColor(ContextCompat.getColor(requireContext(), colorRes))
        binding.statusTv.setTextColor(ContextCompat.getColor(requireContext(), colorRes))
        binding.circularDeterminativePb.progress = progress
        binding.circularDeterminativePb.progressTintList =
            ContextCompat.getColorStateList(requireContext(), colorRes)
    }

    /**
     * 共用污染物顯示邏輯
     */
    fun bindPollution(
        title: String,
        value: Double?,
        itemView: ItemPollutionBinding,
        type: PollutionType,
    ) {
        if (value == null) {
            itemView.tvValue.text = "--"
            itemView.tvStatus.text = "無資料"
            itemView.statusLine.setBackgroundColor(
                ContextCompat.getColor(requireContext(), R.color.light_gray),
            )
            return
        }

        val (status, colorRes) = type.getStatus(value)

        itemView.tvTitle.text = title
        itemView.tvValue.text = value.toString()
        itemView.tvStatus.text = status
        itemView.statusLine.setBackgroundColor(
            ContextCompat.getColor(requireContext(), colorRes),
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
