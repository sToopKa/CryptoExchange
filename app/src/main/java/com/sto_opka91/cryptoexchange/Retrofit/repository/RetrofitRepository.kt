package com.sto_opka91.cryptoexchange.Retrofit.repository


import com.sto_opka91.cryptoexchange.Retrofit.RetrofitInstance
import com.sto_opka91.cryptoexchange.data.AllCoins
import com.sto_opka91.cryptoexchange.data.Marckets
import com.sto_opka91.cryptoexchange.data.MarketForUrl
import retrofit2.Response

class RetrofitRepository {
    suspend fun getAllCoins(start: Int, end: Int): Response<AllCoins> {
        return RetrofitInstance.api.getCoins(start, end)
    }
    suspend fun getAllMarkets(id: Int): Response<Marckets> {
        return RetrofitInstance.api.getMarkets(id)
    }
    suspend fun getMarketsForUrl(): Response<MarketForUrl> {
        return RetrofitInstance.api.getMarketsForUrl()
    }
}