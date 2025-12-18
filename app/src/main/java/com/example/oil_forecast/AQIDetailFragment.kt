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

class AQIDetailFragment : DialogFragment() {
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

        geocode = requireArguments().getParcelable(ARG_GEOCODE)!!

        initView()
    }

    private fun initView() {
        Toast.makeText(requireContext(), "更新時間 ${geocode.publishTime}", Toast.LENGTH_SHORT).show()
        bindAQI(geocode.county + geocode.siteName, geocode.aqi, geocode.status)

        bindPollution(
            title = "PM2.5",
            value = geocode.pm2_5,
            itemView = binding.itemPm25,
            type = PollutionType.PM25,
        )

        bindPollution(
            title = "PM10",
            value = geocode.pm10,
            itemView = binding.itemPm10,
            type = PollutionType.PM10,
        )

        bindPollution(
            title = "O₃",
            value = geocode.o3,
            itemView = binding.itemO3,
            type = PollutionType.O3,
        )

        bindPollution(
            title = "NO₂",
            value = geocode.no2,
            itemView = binding.itemNo2,
            type = PollutionType.NO2,
        )

        bindPollution(
            title = "CO",
            value = geocode.co,
            itemView = binding.itemCo,
            type = PollutionType.CO,
        )

        bindPollution(
            title = "SO₂",
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
    private fun bindPollution(
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

    companion object {
        private const val ARG_GEOCODE = "arg_geocode"

        fun newInstance(geocode: AQIEntity) =
            AQIDetailFragment().apply {
                arguments = bundleOf(ARG_GEOCODE to geocode)
            }
    }
}

enum class PollutionType {
    PM25 {
        override fun getStatus(value: Double) =
            when {
                value <= 15 -> "良好" to R.color.aqi_green
                value <= 35 -> "普通" to R.color.aqi_yellow
                value <= 54 -> "對敏感族群不健康" to R.color.aqi_orange
                value <= 150 -> "對所有族群不健康" to R.color.aqi_red
                value <= 250 -> "非常不健康" to R.color.aqi_purple
                else -> "危害" to R.color.aqi_brown
            }
    },

    PM10 {
        override fun getStatus(value: Double) =
            when {
                value <= 50 -> "良好" to R.color.aqi_green
                value <= 100 -> "普通" to R.color.aqi_yellow
                value <= 254 -> "對敏感族群不健康" to R.color.aqi_orange
                value <= 354 -> "對所有族群不健康" to R.color.aqi_red
                value <= 424 -> "非常不健康" to R.color.aqi_purple
                else -> "危害" to R.color.aqi_brown
            }
    },

    O3 {
        override fun getStatus(value: Double) =
            when {
                value <= 54 -> "良好" to R.color.aqi_green
                value <= 70 -> "普通" to R.color.aqi_yellow
                value <= 85 -> "對敏感族群不健康" to R.color.aqi_orange
                value <= 105 -> "對所有族群不健康" to R.color.aqi_red
                value <= 200 -> "非常不健康" to R.color.aqi_purple
                else -> "危害" to R.color.aqi_brown
            }
    },

    NO2 {
        override fun getStatus(value: Double) =
            when {
                value <= 53 -> "良好" to R.color.aqi_green
                value <= 100 -> "普通" to R.color.aqi_yellow
                value <= 360 -> "對敏感族群不健康" to R.color.aqi_orange
                value <= 649 -> "對所有族群不健康" to R.color.aqi_red
                value <= 1249 -> "非常不健康" to R.color.aqi_purple
                else -> "危害" to R.color.aqi_brown
            }
    },
    CO {
        override fun getStatus(value: Double) =
            when {
                value <= 4 -> "良好" to R.color.aqi_green
                value <= 9 -> "普通" to R.color.aqi_yellow
                value <= 12 -> "對敏感族群不健康" to R.color.aqi_orange
                value <= 15 -> "對所有族群不健康" to R.color.aqi_red
                value <= 30 -> "非常不健康" to R.color.aqi_purple
                else -> "危害" to R.color.aqi_brown
            }
    },
    SO2 {
        override fun getStatus(value: Double) =
            when {
                value <= 35 -> "良好" to R.color.aqi_green
                value <= 75 -> "普通" to R.color.aqi_yellow
                value <= 185 -> "對敏感族群不健康" to R.color.aqi_orange
                value <= 304 -> "對所有族群不健康" to R.color.aqi_red
                value <= 604 -> "非常不健康" to R.color.aqi_purple
                else -> "危害" to R.color.aqi_brown
            }
    }, ;

    abstract fun getStatus(value: Double): Pair<String, Int>
}
