package com.example.oil_forecast

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.oil_forecast.Extension.launchAndRepeatWithViewLifecycle
import com.example.oil_forecast.ViewModels.OilPriceViewModel
import com.example.oil_forecast.databinding.FragmentOilpriceBinding
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class OilPriceFragment : Fragment() {
    private var _binding: FragmentOilpriceBinding? = null
    private val binding get() = _binding!!

    private val viewModel: OilPriceViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentOilpriceBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        launchAndRepeatWithViewLifecycle {
            launch {
                viewModel.oilPriceList.collect { oilPriceList ->
                    if (oilPriceList.isEmpty()) {
                        return@collect
                    }

                    binding.oilPriceTitle.text = oilPriceList[0].name
                    binding.price98.text = oilPriceList[0].price98
                    binding.price95.text = oilPriceList[0].price95
                    binding.price92.text = oilPriceList[0].price92
                    binding.priceDiesel.text = oilPriceList[0].diesel

                    binding.oilPriceTitleFpcc.text = oilPriceList[1].name
                    binding.price98Fpcc.text = oilPriceList[1].price98
                    binding.price95Fpcc.text = oilPriceList[1].price95
                    binding.price92Fpcc.text = oilPriceList[1].price92
                    binding.priceDieselFpcc.text = oilPriceList[1].diesel
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
