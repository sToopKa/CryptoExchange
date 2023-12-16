package com.sto_opka91.cryptoexchange.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query


@Dao
interface DatabaseDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(coin: DataForResearchIdDatabase)
    @Delete
    suspend fun delete (coin: DataForResearchIdDatabase)
    @Query("DELETE FROM notes_table WHERE title = :valueToDelete")
    suspend fun deleteWithName (valueToDelete: String)

    @Query("SELECT * FROM notes_table ")
    fun getAllCoinFromDB(): LiveData<List<DataForResearchIdDatabase>>
}