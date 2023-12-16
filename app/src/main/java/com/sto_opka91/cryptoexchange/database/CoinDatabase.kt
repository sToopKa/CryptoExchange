package com.sto_opka91.cryptoexchange.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.sto_opka91.cryptoexchange.DATABASE_NAME

@Database(entities = arrayOf(DataForResearchIdDatabase::class), version = 1, exportSchema = false)
abstract class CoinDatabase : RoomDatabase() {

    abstract fun getDatabaseDao(): DatabaseDao
    companion object{
        @Volatile
        private var INSTANCE: CoinDatabase?=null

        fun getDatabase(context: Context): CoinDatabase{
            return INSTANCE?: synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    CoinDatabase::class.java,
                    DATABASE_NAME
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}