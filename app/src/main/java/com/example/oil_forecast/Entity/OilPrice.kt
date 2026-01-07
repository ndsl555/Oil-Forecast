package com.example.oil_forecast.Entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class OilPrice(
    val name: String,
    val price92: String,
    val price95: String,
    val price98: String,
    val diesel: String,
) : Parcelable
