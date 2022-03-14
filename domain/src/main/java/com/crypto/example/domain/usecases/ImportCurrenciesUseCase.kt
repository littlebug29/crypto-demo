package com.crypto.example.domain.usecases

import android.content.Context
import com.crypto.example.domain.repositories.CurrencyListRepository

class ImportCurrenciesUseCase(
    private val currencyListRepository: CurrencyListRepository
) {
    suspend operator fun invoke(context: Context): Boolean {
        val currencies = currencyListRepository.loadCurrenciesFromAssetFile(context)
        return if (currencies.isEmpty()) {
            false
        } else {
            currencyListRepository.importCurrenciesIntoDatabase(context, currencies)
        }
    }
}