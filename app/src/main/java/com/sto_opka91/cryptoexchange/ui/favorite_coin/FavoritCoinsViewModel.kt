package com.sto_opka91.cryptoexchange.ui.favorite_coin

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.sto_opka91.cryptoexchange.database.CoinDatabase
import com.sto_opka91.cryptoexchange.database.DataForResearchIdDatabase
import com.sto_opka91.cryptoexchange.database.DatabaseRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FavoritCoinsViewModel(application: Application): AndroidViewModel(application) {
    private val repository: DatabaseRepository
    val allCoinsFromDB : LiveData<List<DataForResearchIdDatabase>>
    init{
        val dao = CoinDatabase.getDatabase(application).getDatabaseDao()
        repository = DatabaseRepository(dao)
        allCoinsFromDB = repository.allCoinsFromDB
    }

    fun deleteCoin(coin: DataForResearchIdDatabase) = viewModelScope.launch(Dispatchers.IO) {
        repository.delete(coin)
    }
    fun insertCoin(coin: DataForResearchIdDatabase) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(coin)
    }
}