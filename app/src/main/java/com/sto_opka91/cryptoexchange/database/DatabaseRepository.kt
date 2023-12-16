package com.sto_opka91.cryptoexchange.database

import androidx.lifecycle.LiveData

class DatabaseRepository(private val DatabaseDao: DatabaseDao) {
    val allCoinsFromDB : LiveData<List<DataForResearchIdDatabase>> = DatabaseDao.getAllCoinFromDB()
    suspend fun insert(coin: DataForResearchIdDatabase){
        DatabaseDao.insert(coin)
    }
    suspend fun delete(coin: DataForResearchIdDatabase){
        DatabaseDao.delete(coin)
    }
    suspend fun deleteWithName(valueToDelete: String){
        DatabaseDao.deleteWithName(valueToDelete)
    }

}