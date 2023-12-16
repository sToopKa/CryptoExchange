package com.sto_opka91.cryptoexchange.Retrofit

import com.sto_opka91.cryptoexchange.data.AllCoins
import com.sto_opka91.cryptoexchange.data.Marckets
import com.sto_opka91.cryptoexchange.data.MarketForUrl
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("api/tickers/")
    suspend fun getCoins(@Query("start") start: Int, @Query("limit") end: Int): Response<AllCoins>
    @GET("api/coin/markets/")
    suspend fun getMarkets(@Query("id") id: Int): Response<Marckets>
    @GET("api/exchanges/")
    suspend fun getMarketsForUrl(): Response<MarketForUrl>
}