package com.example.oil_forecast

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
