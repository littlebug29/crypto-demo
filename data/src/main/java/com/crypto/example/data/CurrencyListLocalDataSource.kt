package com.crypto.example.data

import com.crypto.example.data.database.CurrencyDao
import com.crypto.example.data.database.LocalCurrency
import com.crypto.example.domain.CurrencyEntity

class CurrencyListLocalDataSource(
    private val currencyDao: CurrencyDao
) {
    suspend fun loadAllCurrencies(): List<CurrencyEntity> =
        currencyDao.loadAllCurrencies()

    suspend fun importCurrencies(newCurrencies: List<LocalCurrency>) =
        currencyDao.insertCurrencies(newCurrencies)
}