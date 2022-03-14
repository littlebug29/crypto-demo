package com.crypto.example.domain.usecases

import com.crypto.example.domain.CurrencyEntity
import com.crypto.example.domain.repositories.CurrencyListRepository
import com.crypto.example.domain.repositories.DataResult

class LoadCurrenciesUseCase(
    private val currencyListRepository: CurrencyListRepository
) {
    suspend operator fun invoke(): DataResult<List<CurrencyEntity>> =
        currencyListRepository.loadAllCurrencies()
}