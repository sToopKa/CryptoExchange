package com.sto_opka91.cryptoexchange.ui.detailCoin

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.sto_opka91.cryptoexchange.MARKET_URL
import com.sto_opka91.cryptoexchange.NEWS_URL
import com.sto_opka91.cryptoexchange.Retrofit.repository.RetrofitRepository
import com.sto_opka91.cryptoexchange.data.MarcketsItem
import com.sto_opka91.cryptoexchange.data.MarketUrl
import com.sto_opka91.cryptoexchange.data.News
import com.sto_opka91.cryptoexchange.database.CoinDatabase
import com.sto_opka91.cryptoexchange.database.DataForResearchIdDatabase
import com.sto_opka91.cryptoexchange.database.DatabaseRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import org.json.JSONObject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine


class DetailViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = RetrofitRepository()
    private val repositoryDB: DatabaseRepository
    var marketForAdd: MutableLiveData<ArrayList<MarcketsItem>> = MutableLiveData()
    val liveNewsList = MutableLiveData<List<News>>()
    val allCoinsFromDB : LiveData<List<DataForResearchIdDatabase>>
    init{
        val dao = CoinDatabase.getDatabase(application).getDatabaseDao()
        repositoryDB = DatabaseRepository(dao)
        allCoinsFromDB = repositoryDB.allCoinsFromDB
    }
    fun deleteCoin(name :String) = viewModelScope.launch(Dispatchers.IO) {
        repositoryDB.deleteWithName(name)
    }
    fun insertCoin(coin: DataForResearchIdDatabase) = viewModelScope.launch(Dispatchers.IO) {
        repositoryDB.insert(coin)
    }


    // Создаем CoroutineScope для работы с корутинами
    private val viewModelScope = CoroutineScope(Dispatchers.Main)

    // Выполняем корутину в viewModelScope
    fun getMarketsRetrofit(id: Int) {
        viewModelScope.launch {
            try {
                // Асинхронно получаем данные по рынкам
                val marketsResponse = async { repository.getAllMarkets(id) }

                // Асинхронно получаем данные по URL с помощью Volley
                val marketUrls = async { getMarketUrls() }

                // Дожидаемся завершения обоих запросов
                if (marketsResponse.await().isSuccessful && marketUrls.await().isNotEmpty()) {
                    // Обрабатываем данные
                    val marketsItems = marketsResponse.await().body() ?: emptyList()
                    val marketUrlsList = marketUrls.await()

                    // Обновляем URL в MarketsItem на основе совпадения имен
                    val marketForAddList = ArrayList<MarcketsItem>()
                    for (marketItem in marketsItems) {
                        val marketUrl = marketUrlsList.find { it.name == marketItem.name }
                        if (marketUrl != null) {
                            marketForAddList.add(
                                MarcketsItem(
                                    marketItem.base,
                                    marketItem.name,
                                    marketItem.price,
                                    marketItem.price_usd,
                                    marketItem.quote,
                                    marketItem.time,
                                    marketItem.volume,
                                    marketItem.volume_usd,
                                    marketUrl.url
                                )
                            )
                        }
                    }
                    // Уведомляем о завершении загрузки данных
                    marketForAdd.postValue(marketForAddList)
                } else {
                    Log.e("myLog", "Markets request or Market URLs request failed")
                }
            } catch (e: Exception) {
                Log.e("myLog", "${e.message}")
            }
        }
    }

    private suspend fun getMarketUrls(): List<MarketUrl> = suspendCoroutine { continuation ->
        val url = MARKET_URL
        val queue = Volley.newRequestQueue(getApplication())
        val request = StringRequest(
            Request.Method.GET,
            url,
            { result ->
                val jsonObject = JSONObject(result)
                continuation.resume(parseUrlMarketInto(jsonObject))
            },
            { error ->
                continuation.resumeWithException(error)
            }
        )
        queue.add(request)
    }

    private fun parseUrlMarketInto(jsonObj: JSONObject): List<MarketUrl> {
        val marketUrls = mutableListOf<MarketUrl>()

        for (key in jsonObj.keys()) {
            val value = jsonObj.getJSONObject(key)
            val id = value.getString("id")
            val name = value.getString("name")
            val nameId = value.getString("name_id")
            val volumeUsd = ""
            val activePairs = ""
            val url = value.getString("url")
            val country = value.getString("country")
            val marketUrl = MarketUrl(id, name, nameId, volumeUsd, activePairs, url, country)
            marketUrls.add(marketUrl)
        }

        return marketUrls
    }

    fun getNewsForCoin(symbol: String){
        val url = "$NEWS_URL+$symbol"
        val queue = Volley.newRequestQueue(getApplication())
        val request = StringRequest(
            Request.Method.GET,
            url,
            { result ->
                val jsonObject = JSONObject(result)
                parseNews(jsonObject)
            },
            { error ->
                Log.d("myLog", error.message.toString())
            }
        )
        queue.add(request)
    }
    private fun parseNews(mainObject: JSONObject){
        val list = ArrayList<News>()
        val newsArray = mainObject.getJSONArray("results")

        for(i in 0 until newsArray.length()){
            val news = newsArray[i] as JSONObject
            val parsingNews = News(
                news.getString("title"),
                news.getString("url"),
                news.getString("published_at")
            )
            list.add(parsingNews)
        }
       liveNewsList.value = list
    }

}

