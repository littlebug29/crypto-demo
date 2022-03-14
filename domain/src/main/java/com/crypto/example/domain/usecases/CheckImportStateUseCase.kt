package com.crypto.example.domain.usecases

import android.content.Context
import com.crypto.example.domain.repositories.CurrencyListRepository

class CheckImportStateUseCase(
    private val currencyListRepository: CurrencyListRepository
) {
    suspend operator fun invoke(context: Context): Boolean =
        currencyListRepository.shouldImportData(context)
}