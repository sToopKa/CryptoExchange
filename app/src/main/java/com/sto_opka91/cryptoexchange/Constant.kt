package com.sto_opka91.cryptoexchange

import android.graphics.Color
import com.sto_opka91.cryptoexchange.data.Data
import com.sto_opka91.cryptoexchange.data.Icons


const val BASE_URL = " https://api.coinlore.net/"
const val MARKET_URL = " https://api.coinlore.net/api/exchanges/"
const val PNG_URL = "https://rest.coinapi.io/v1/assets/icons/32/apikey-B797886A-C9CB-4599-9403-709939E185F3"
const val NEWS_URL = "https://cryptopanic.com/api/v1/posts/?auth_token=7fca5f255c8b3f1cd65ade8e46b67a044a6f3974&currencies="
const val NEWS_URL_MAIN = "https://cryptopanic.com/api/v1/posts/?auth_token=7fca5f255c8b3f1cd65ade8e46b67a044a6f3974&filter=rising"
const val COLOR = Color.RED
var LIST_OF_NAMES_COINS = ArrayList<Data>()
var LIST_OF_ICONS_COINS = ArrayList<Icons>()
const val DATABASE_NAME = "coin_database"