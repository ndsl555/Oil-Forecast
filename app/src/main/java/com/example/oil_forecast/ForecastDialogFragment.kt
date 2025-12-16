package com.example.oil_forecast

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.oil_forecast.Extension.launchAndRepeatWithViewLifecycle
import com.example.oil_forecast.ViewModels.WeatherViewModel
import com.example.oil_forecast.databinding.FragmentForecastDialogBinding
import com.example.oil_forecast.ui.Adapter.ForecastAdapter
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class ForecastDialogFragment : DialogFragment(), View.OnClickListener {
    private var _binding: FragmentForecastDialogBinding? = null
    private val binding get() = _binding!!

    private val viewModel: WeatherViewModel by viewModel()

    private lateinit var adapter: ForecastAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentForecastDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)

        val geocode = requireArguments().getString(ARG_GEOCODE)!!

        adapter = ForecastAdapter()
        viewModel.fetchForecast(geocode)
        initParam()
        initView()
    }

    private fun initView() {
        binding.fabRefresh.setOnClickListener(this)
        binding.rvForecast.apply {
            layoutManager = LinearLayoutManager(
                requireContext(),
                LinearLayoutManager.HORIZONTAL, // 改成橫向
                false
            )
            adapter = this@ForecastDialogFragment.adapter
        }
    }

    private fun initParam() {
        launchAndRepeatWithViewLifecycle {
            launch {
                viewModel.forecast.collect { list ->
                    adapter.submit(list)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onClick(p0: View) {
        when (p0.id) {
            R.id.fabRefresh -> dismiss()
        }
    }

    companion object {
        private const val ARG_GEOCODE = "arg_geocode"

        fun newInstance(geocode: String) =
            ForecastDialogFragment().apply {
                arguments = bundleOf(ARG_GEOCODE to geocode)
            }
    }
}
