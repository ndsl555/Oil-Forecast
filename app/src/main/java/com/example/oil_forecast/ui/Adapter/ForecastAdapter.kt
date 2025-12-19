package com.example.oil_forecast.ui.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.oil_forecast.Entity.ForecastEntity
import com.example.oil_forecast.databinding.ItemForecastBinding
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

class ForecastAdapter :
    ListAdapter<ForecastEntity, ForecastAdapter.VH>(DiffCallback) {
    inner class VH(val binding: ItemForecastBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): VH {
        val binding =
            ItemForecastBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false,
            )
        return VH(binding)
    }

    override fun onBindViewHolder(
        holder: VH,
        position: Int,
    ) {
        val item = getItem(position)

        holder.binding.textDate.text =
            "${formatTime(item.startTime)} ~ ${formatTime(item.endTime)}"

        println("${formatTime(item.startTime)} ~ ${formatTime(item.endTime)}")
        holder.binding.textTemp.text =
            "${item.minTemp ?: "--"} ~ ${item.maxTemp ?: "--"}°C"

        holder.binding.textRain.text =
            "${item.pop?.let { "$it%" } ?: "--"}"

        holder.binding.textWind.text =
            "${item.windDirection + item.windSpeed + "級" ?: "--"}"

        holder.binding.textUV.text =
            "${item.uVExposureLevel ?: "--"}"

        holder.binding.circularDeterminativePb.progress =
            (item.relativeHumidity?.toInt() ?: 0) * 100 / 100

        holder.binding.progressTv.text =
            item.relativeHumidity?.let { "$it%" } ?: "--"
    }

    fun formatTime(time: String): String {
        return try {
            // 解析成 OffsetDateTime
            val odt = OffsetDateTime.parse(time)
            // 格式化成 MM-dd HH:mm
            odt.format(DateTimeFormatter.ofPattern("MM-dd HH:mm"))
        } catch (e: Exception) {
            // 如果解析失敗，就手動去掉 T、秒數和時區
            time.replace("T", " ") // 去掉 T
                .substringBefore("+") // 去掉 +08:00
                .take(16) // 取前 16 個字元或字串結尾
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<ForecastEntity>() {
        override fun areItemsTheSame(
            oldItem: ForecastEntity,
            newItem: ForecastEntity,
        ): Boolean = oldItem.startTime == newItem.startTime && oldItem.endTime == newItem.endTime

        override fun areContentsTheSame(
            oldItem: ForecastEntity,
            newItem: ForecastEntity,
        ): Boolean = oldItem == newItem
    }
}
