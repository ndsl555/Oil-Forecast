package com.example.oil_forecast.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.oil_forecast.databinding.ItemLocationButtonBinding

class LocationAdapter(
    private val onClick: (geocode: String) -> Unit,
) : RecyclerView.Adapter<LocationAdapter.VH>() {
    private val data = mutableListOf<Pair<String, String>>()

    fun submit(list: List<Pair<String, String>>) {
        data.clear()
        data.addAll(list)
        notifyDataSetChanged()
    }

    inner class VH(
        val binding: ItemLocationButtonBinding,
    ) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): VH {
        val binding =
            ItemLocationButtonBinding.inflate(
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
        val (name, geocode) = data[position]

        holder.binding.btnLocation.text = name
        holder.binding.btnLocation.setOnClickListener {
            onClick(geocode)
        }
    }

    override fun getItemCount(): Int = data.size
}
