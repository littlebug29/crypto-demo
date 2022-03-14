package com.crypto.example.data.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [LocalCurrency::class], version = 1)
abstract class CurrencyDatabase: RoomDatabase() {
    abstract fun currencyDao(): CurrencyDao
}