package com.example.oil_forecast

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import com.example.oil_forecast.Entity.AQIEntity
import com.example.oil_forecast.databinding.FragmentDetailDialogBinding
import com.example.oil_forecast.databinding.ItemPollutionBinding

class AQIDetailFragment : DialogFragment(), View.OnClickListener {
    private var _binding: FragmentDetailDialogBinding? = null
    private val binding get() = _binding!!

    private lateinit var geocode: AQIEntity

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentDetailDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)

        geocode = requireArguments().getParcelable(ARG_AQIITEM)!!

        initView()
    }

    private fun initView() {
        binding.floatingActionButton.setOnClickListener(this)
        Toast.makeText(requireContext(), "更新時間 ${geocode.publishTime}", Toast.LENGTH_SHORT).show()
        bindAQI(geocode.county + geocode.siteName, geocode.aqi, geocode.status)

        bindPollution(
            title = "PM2.5 (μg/m³)",
            value = geocode.pm2_5,
            itemView = binding.itemPm25,
            type = PollutionType.PM25,
        )

        bindPollution(
            title = "PM10 (μg/m³)",
            value = geocode.pm10,
            itemView = binding.itemPm10,
            type = PollutionType.PM10,
        )

        bindPollution(
            title = "O₃ (μg/m³)",
            value = geocode.o3,
            itemView = binding.itemO3,
            type = PollutionType.O3,
        )

        bindPollution(
            title = "NO₂ (μg/m³)",
            value = geocode.no2,
            itemView = binding.itemNo2,
            type = PollutionType.NO2,
        )

        bindPollution(
            title = "CO (μg/m³)",
            value = geocode.co,
            itemView = binding.itemCo,
            type = PollutionType.CO,
        )

        bindPollution(
            title = "SO₂ (μg/m³)",
            value = geocode.so2,
            itemView = binding.itemSo2,
            type = PollutionType.SO2,
        )

        println(geocode)
        binding.progressTv.text = geocode.aqi?.toString() ?: "--"
    }

    private fun bindAQI(
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
        binding.progressTv.text = "$value%"
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

    override fun onClick(v: View) {
        when (v.id) {
            R.id.floatingActionButton -> dismiss()
        }
    }

    companion object {
        private const val ARG_AQIITEM = "arg_aqiItem"

        fun newInstance(geocode: AQIEntity) =
            AQIDetailFragment().apply {
                arguments = bundleOf(ARG_AQIITEM to geocode)
            }
    }
}
