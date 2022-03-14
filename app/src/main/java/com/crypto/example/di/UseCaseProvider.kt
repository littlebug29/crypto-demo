package com.crypto.example.di

import com.crypto.example.domain.repositories.CurrencyListRepository
import com.crypto.example.domain.usecases.CheckImportStateUseCase
import com.crypto.example.domain.usecases.ImportCurrenciesUseCase
import com.crypto.example.domain.usecases.LoadCurrenciesUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object UseCaseProvider {

    @ViewModelScoped
    @Provides
    fun provideLoadCurrenciesUseCase(
        currencyListRepository: CurrencyListRepository
    ): LoadCurrenciesUseCase = LoadCurrenciesUseCase(currencyListRepository)

    @ViewModelScoped
    @Provides
    fun provideImportCurrenciesUseCase(
        currencyListRepository: CurrencyListRepository
    ): ImportCurrenciesUseCase = ImportCurrenciesUseCase(currencyListRepository)

    @ViewModelScoped
    @Provides
    fun provideCheckImportStateUseCase(
        currencyListRepository: CurrencyListRepository
    ): CheckImportStateUseCase = CheckImportStateUseCase(currencyListRepository)
}