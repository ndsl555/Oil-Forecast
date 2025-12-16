package com.example.oil_forecast.NetWork

sealed class ForeCastApiPath {
    abstract val path: String

    data object GetAllForeCast : ForeCastApiPath() {
        const val PATH = "F-D0047-091?Authorization=rdec-key-123-45678-011121314"
        override val path: String = PATH
    }
}
