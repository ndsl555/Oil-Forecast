package com.example.oil_forecast.ui.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.oil_forecast.Entity.ForecastEntity
import com.example.oil_forecast.databinding.ItemForecastBinding
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

class ForecastAdapter :
    RecyclerView.Adapter<ForecastAdapter.VH>() {
    private val data = mutableListOf<ForecastEntity>()

    fun submit(list: List<ForecastEntity>) {
        data.clear()
        data.addAll(list)
        notifyDataSetChanged()
    }

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
        val item = data[position]

        holder.binding.textdate.text =
            "${formatTime(item.startTime)} ~ ${formatTime(item.endTime)}"

        println("${formatTime(item.startTime)} ~ ${formatTime(item.endTime)}")
        holder.binding.texttem.text =
            "溫度：${item.minTemp ?: "--"} ~ ${item.maxTemp ?: "--"}°C"

        holder.binding.textrain.text =
            "降雨：${item.pop?.let { "$it%" } ?: "--"}"

        holder.binding.textfeel.text =
            "天氣：${item.weather ?: "--"}"

        holder.binding.texthumid.text =
            "平均：${item.avgTemp ?: "--"}°C"
    }

    fun formatTime(time: String): String {
        return try {
            // 解析成 OffsetDateTime
            val odt = OffsetDateTime.parse(time)
            // 格式化成 MM-dd HH:mm
            odt.format(DateTimeFormatter.ofPattern("MM-dd HH:mm"))
        } catch (e: Exception) {
            // 如果解析失敗，就手動去掉 T、秒數和時區
            time.replace("T", " ")        // 去掉 T
                .substringBefore("+")      // 去掉 +08:00
                .take(16)                  // 取前 16 個字元或字串結尾
        }
    }


    override fun getItemCount() = data.size
}
