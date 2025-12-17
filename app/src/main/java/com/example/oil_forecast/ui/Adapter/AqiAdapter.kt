package com.example.oil_forecast.ui.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.oil_forecast.Entity.AQIEntity
import com.example.oil_forecast.R
import com.example.oil_forecast.databinding.ItemAqiBinding

class AqiAdapter : RecyclerView.Adapter<AqiAdapter.VH>() {
    private val data = mutableListOf<AQIEntity>()

    fun submit(list: List<AQIEntity>) {
        data.clear()
        data.addAll(list)
        notifyDataSetChanged()
    }

    inner class VH(val binding: ItemAqiBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): VH {
        val binding = ItemAqiBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VH(binding)
    }

    override fun onBindViewHolder(
        holder: VH,
        position: Int,
    ) {
        val item = data[position]
        holder.binding.tvRank.text = (position + 1).toString()
        holder.binding.tvCountry.text = item.county
        holder.binding.tvSiteName.text = item.siteName
        holder.binding.tvAqiValue.text = item.aqi.toString()
        // 根據 AQI 狀態改變文字顏色
        val context = holder.itemView.context
        val color =
            when (item.status) {
                "良好" -> ContextCompat.getColor(context, R.color.aqi_green)
                "普通" -> ContextCompat.getColor(context, R.color.aqi_yellow)
                "對敏感族群不健康" -> ContextCompat.getColor(context, R.color.aqi_orange)
                "對所有族群不健康" -> ContextCompat.getColor(context, R.color.aqi_red)
                "非常不健康" -> ContextCompat.getColor(context, R.color.aqi_purple)
                "危害" -> ContextCompat.getColor(context, R.color.aqi_brown)
                else -> ContextCompat.getColor(context, R.color.black)
            }
        holder.binding.tvAqiValue.setTextColor(color)
    }

    override fun getItemCount() = data.size
}
