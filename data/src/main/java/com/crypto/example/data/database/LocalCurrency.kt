package com.crypto.example.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.crypto.example.domain.CurrencyEntity

@Entity(tableName = "currencies")
data class LocalCurrency(
    @PrimaryKey
    override val id: String,
    override val name: String,
    override val symbol: String
) : CurrencyEntity