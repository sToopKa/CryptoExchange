package com.sto_opka91.cryptoexchange.data

import java.math.BigDecimal

data class MarketUrl(
    val id: String,
    val name: String,
    val name_id: String,
    val volume_usd: String,
    val active_pairs: String,
    val url: String,
    val country: String
)
