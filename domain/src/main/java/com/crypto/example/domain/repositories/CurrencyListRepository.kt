package com.crypto.example.domain.repositories

import android.content.Context
import com.crypto.example.domain.CurrencyEntity

interface CurrencyListRepository {
    suspend fun loadAllCurrencies(): DataResult<List<CurrencyEntity>>

    suspend fun shouldImportData(context: Context): Boolean

    suspend fun loadCurrenciesFromAssetFile(context: Context): List<CurrencyEntity>

    suspend fun importCurrenciesIntoDatabase(
        context: Context,
        currencyEntities: List<CurrencyEntity>
    ): Boolean
}