package com.example.oil_forecast

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.oil_forecast.Extension.launchAndRepeatWithViewLifecycle
import com.example.oil_forecast.ViewModels.AQIViewModel
import com.example.oil_forecast.databinding.FragmentAqiBinding
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

        initView()
        initParam()

//        viewModel.fetchAQIs()
    }

    private fun initView() {
        binding.btnTest2.setOnClickListener {
            val action = AQIFragmentDirections.actionNavigationAQIFragmentToAqiListFragment()
            findNavController().navigate(action)
        }
    }

    private fun initParam() {
        launchAndRepeatWithViewLifecycle {
            viewModel.aqiSortList.collect { locations ->
                binding.title1.text = getString(R.string.no_1_display, locations.getOrNull(0) ?: getString(R.string.empty_dash))
                binding.title2.text = getString(R.string.no_2_display, locations.getOrNull(1) ?: getString(R.string.empty_dash))
                binding.title3.text = getString(R.string.no_3_display, locations.getOrNull(2) ?: getString(R.string.empty_dash))
                binding.title4.text = getString(R.string.no_1_display, locations.getOrNull(3) ?: getString(R.string.empty_dash))
                binding.title5.text = getString(R.string.no_2_display, locations.getOrNull(4) ?: getString(R.string.empty_dash))
                binding.title6.text = getString(R.string.no_3_display, locations.getOrNull(5) ?: getString(R.string.empty_dash))
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
