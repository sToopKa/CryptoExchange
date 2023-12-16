package com.sto_opka91.cryptoexchange.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "notes_table")
data class DataForResearchIdDatabase(
    @PrimaryKey(autoGenerate = true)
    val id: Int?,
    @ColumnInfo(name = "idCoin")
    val idCoin: String?,
    @ColumnInfo(name = "title")
    val title: String?

) : Serializable

