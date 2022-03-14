package com.crypto.example

import android.os.Parcelable
import com.crypto.example.domain.CurrencyEntity
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CurrencyUiInfo(
    override val id: String,
    override val name: String,
    override val symbol: String
) : CurrencyEntity, Parcelable {
    companion object {
        fun fromEntity(entity: CurrencyEntity) =
            CurrencyUiInfo(entity.id, entity.name, entity.symbol)
    }
}
