package com.crypto.example.data.di

import android.content.Context
import androidx.room.Room
import com.crypto.example.data.CurrencyListLocalDataSource
import com.crypto.example.data.CurrencyListRepositoryImpl
import com.crypto.example.data.database.CurrencyDatabase
import com.crypto.example.domain.repositories.CurrencyListRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {
    @Singleton
    @Provides
    fun provideLocalDatabase(@ApplicationContext context: Context): CurrencyDatabase = Room
        .databaseBuilder(
            context,
            CurrencyDatabase::class.java,
            "currency-database"
        ).build()

    @Singleton
    @Provides
    fun provideDataSource(currencyDatabase: CurrencyDatabase): CurrencyListLocalDataSource =
        CurrencyListLocalDataSource(currencyDatabase.currencyDao())

    @Singleton
    @Provides
    fun provideDispatcher(): CoroutineDispatcher = Dispatchers.IO
}


@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {
    @Singleton
    @Binds
    fun bindCurrenciesRepository(currenciesRepositoryImpl: CurrencyListRepositoryImpl): CurrencyListRepository
}
