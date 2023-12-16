package com.sto_opka91.cryptoexchange.data

data class MarcketsItem(
    val base: String,
    val name: String,
    val price: Double,
    val price_usd: Double,
    val quote: String,
    val time: Int,
    val volume: Double,
    val volume_usd: Double,
    val urlMarket: String
)