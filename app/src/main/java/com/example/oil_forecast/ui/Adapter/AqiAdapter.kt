package com.example.oil_forecast.ui.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.oil_forecast.Entity.AQIEntity
import com.example.oil_forecast.R
import com.example.oil_forecast.databinding.ItemAqiBinding
import com.example.oil_forecast.databinding.ItemAqiHeaderBinding

sealed class AqiListItem {
    data class HeaderItem(val status: String, val count: Int) : AqiListItem()

    data class AqiItem(val aqiEntity: AQIEntity, val rank: Int) : AqiListItem()
}

class AqiAdapter(
    private val onClick: (item: AQIEntity) -> Unit,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val data = mutableListOf<AqiListItem>()

    fun submit(list: List<AqiListItem>) {
        data.clear()
        data.addAll(list)
        notifyDataSetChanged()
    }

    class HeaderViewHolder(val binding: ItemAqiHeaderBinding) : RecyclerView.ViewHolder(binding.root)

    class ItemViewHolder(val binding: ItemAqiBinding) : RecyclerView.ViewHolder(binding.root)

    override fun getItemViewType(position: Int): Int {
        return when (data[position]) {
            is AqiListItem.HeaderItem -> VIEW_TYPE_HEADER
            is AqiListItem.AqiItem -> VIEW_TYPE_ITEM
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_HEADER -> {
                val binding =
                    ItemAqiHeaderBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false,
                    )
                HeaderViewHolder(binding)
            }
            else -> {
                val binding =
                    ItemAqiBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false,
                    )
                ItemViewHolder(binding)
            }
        }
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int,
    ) {
        when (val listItem = data[position]) {
            is AqiListItem.HeaderItem -> {
                (holder as HeaderViewHolder).binding.headerText.text = "${listItem.status} (${listItem.count})"
            }
            is AqiListItem.AqiItem -> {
                val itemViewHolder = holder as ItemViewHolder
                val aqiEntity = listItem.aqiEntity
                itemViewHolder.binding.tvRank.text = listItem.rank.toString()
                itemViewHolder.binding.tvCountry.text = aqiEntity.county
                itemViewHolder.binding.tvSiteName.text = aqiEntity.siteName
                itemViewHolder.binding.tvAqiValue.text = aqiEntity.aqi.toString()

                val context = itemViewHolder.itemView.context
                val color =
                    when (aqiEntity.status) {
                        "良好" -> ContextCompat.getColor(context, R.color.aqi_green)
                        "普通" -> ContextCompat.getColor(context, R.color.aqi_yellow)
                        "對敏感族群不健康" -> ContextCompat.getColor(context, R.color.aqi_orange)
                        "對所有族群不健康" -> ContextCompat.getColor(context, R.color.aqi_red)
                        "非常不健康" -> ContextCompat.getColor(context, R.color.aqi_purple)
                        "危害" -> ContextCompat.getColor(context, R.color.aqi_brown)
                        else -> ContextCompat.getColor(context, R.color.black)
                    }

                itemViewHolder.binding.tvAqiValue.setTextColor(color)

                itemViewHolder.itemView.setOnClickListener {
                    onClick(aqiEntity)
                }
            }
        }
    }

    override fun getItemCount() = data.size

    companion object {
        internal const val VIEW_TYPE_HEADER = 0
        internal const val VIEW_TYPE_ITEM = 1
    }
}
