package com.sto_opka91.cryptoexchange.ui.home

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.sto_opka91.cryptoexchange.NEWS_URL_MAIN
import com.sto_opka91.cryptoexchange.PNG_URL
import com.sto_opka91.cryptoexchange.Retrofit.repository.RetrofitRepository
import com.sto_opka91.cryptoexchange.data.Data
import com.sto_opka91.cryptoexchange.data.Icons
import com.sto_opka91.cryptoexchange.data.News
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = RetrofitRepository()
    private val _coinsLiveData = MutableLiveData<List<List<Data>>>()
    val coinsLiveData: LiveData<List<List<Data>>> get() = _coinsLiveData
    private val _iconsLiveData = MutableLiveData<List<Icons>?>()
    private val viewModelJob = Job()
    private val viewModelScope = CoroutineScope(Dispatchers.Main + viewModelJob)
    val liveNewsList = MutableLiveData<List<News>>()

    suspend fun getCoinsRetrofitAsync(start: Int, end: Int): List<Data>? {
        val response = repository.getAllCoins(start, end)
        return response.body()?.data
    }

    fun getCoinsRetrofit() {
        viewModelScope.launch {
            try {
                val deferredList = ArrayList<Deferred<List<Data>?>>()
                coroutineScope {
                    for (i in 1..2000 step 100) {
                        val endValue = i + 99
                        val deferred = async { getCoinsRetrofitAsync(i, endValue) }
                        deferredList.add(deferred)
                    }
                }
                val list = deferredList.awaitAll().filterNotNull()
                _coinsLiveData.postValue(list)
                _iconsLiveData.postValue(loadIcons())
            } catch (e: Exception) {
                Log.e("myLog", "${e.message}")
            }
        }
    }

    suspend fun loadIcons(): ArrayList<Icons> = suspendCoroutine { continuation ->
        val url = PNG_URL
        val queue = Volley.newRequestQueue(getApplication())
        val request = StringRequest(
            Request.Method.GET,
            url,
            { result ->
                continuation.resume(parseIconsInto(JSONArray(result)))
            },
            { error ->
                continuation.resumeWithException(error)
            }
        )
        queue.add(request)
    }

    private fun parseIconsInto(result: JSONArray): ArrayList<Icons> {
        try {
            val listIconsPng = ArrayList<Icons>()
            for (i in 0 until result.length()) {
                val item = result[i] as JSONObject
                val icon = Icons(
                    asset_id = item.getString("asset_id"),
                    url = item.getString("url")
                )
                listIconsPng.add(icon)
            }
            return listIconsPng
        } catch (e: Exception) {
            Log.e("myLog", e.message.toString())
            return ArrayList()
        }
    }

    fun getNewsForCoin( ){
        val url = NEWS_URL_MAIN
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


    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}

