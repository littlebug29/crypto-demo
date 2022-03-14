package com.crypto.example

data class CurrencyListState(
    val isFetchingCurrencies: Boolean = false,
    val currencyList: List<CurrencyUiInfo> = emptyList(),
    val errorMessage: String? = null
)

val CurrencyListState.canShowLoadButton: Boolean get() = !isFetchingCurrencies && currencyList.isEmpty()