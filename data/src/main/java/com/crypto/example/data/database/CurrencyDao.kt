package com.crypto.example.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query

@Dao
interface CurrencyDao {
    @Insert(onConflict = REPLACE)
    suspend fun insertCurrencies(currencies: List<LocalCurrency>)

    @Query("SELECT * FROM currencies")
    suspend fun loadAllCurrencies(): List<LocalCurrency>
}