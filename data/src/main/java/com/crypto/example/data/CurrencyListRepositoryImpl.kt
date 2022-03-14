package com.crypto.example.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import com.crypto.example.data.database.LocalCurrency
import com.crypto.example.domain.CurrencyEntity
import com.crypto.example.domain.repositories.CurrencyListRepository
import com.crypto.example.domain.repositories.DataResult
import com.crypto.example.domain.repositories.Error
import com.crypto.example.domain.repositories.Success
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.BufferedReader
import java.io.IOException
import java.lang.Exception
import javax.inject.Inject

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("currency_states")

class CurrencyListRepositoryImpl @Inject constructor(
    private val currenciesLocalDataSource: CurrenciesLocalDataSource,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : CurrencyListRepository {
    override suspend fun loadAllCurrencies(): DataResult<List<CurrencyEntity>> = withContext(dispatcher) {
        return@withContext try {
            val allCurrencies = currenciesLocalDataSource.loadAllCurrencies()
            Success(allCurrencies)
        } catch (exception: Exception) {
            Error(exception)
        }
    }

    override suspend fun shouldImportData(context: Context): Boolean = withContext(dispatcher) {
        context.dataStore.data
            .map {
                it[booleanPreferencesKey(CURRENCY_LOADING_STATE)] ?: false
            }
            .firstOrNull()
            ?: false
    }

    override suspend fun loadCurrenciesFromAssetFile(context: Context): List<CurrencyEntity> =
        withContext(dispatcher) {
            val jsonData = loadJsonDataFromAssetFile(context)
            if (jsonData.isNullOrEmpty()) {
                return@withContext emptyList<CurrencyEntity>()
            } else {
                return@withContext parseJsonData(jsonData)
            }
        }

    private suspend fun loadJsonDataFromAssetFile(context: Context): String? {
        return try {
                context.assets.open(DATA_FILENAME).bufferedReader().use(BufferedReader::readText)
        } catch (ex: IOException) {
            null
        }
    }

    private suspend fun parseJsonData(data: String): List<CurrencyEntity> {
        val jsonObject = JSONObject(data)
        val currencyJsonArray = jsonObject.getJSONArray(KEY_CURRENCY)
        val resultList = mutableListOf<LocalCurrency>()
        for (index in 0 until currencyJsonArray.length()) {
            val currencyJSONObject = currencyJsonArray.getJSONObject(index)
            val id = currencyJSONObject[KEY_ID] as String
            val name = currencyJSONObject[KEY_NAME] as String
            val symbol = currencyJSONObject[KEY_SYMBOL] as String
            resultList.add(LocalCurrency(id, name, symbol))
        }
        return resultList
    }

    override suspend fun importCurrenciesIntoDatabase(
        context: Context,
        currencyEntities: List<CurrencyEntity>
    ): Boolean = withContext(dispatcher) {
        if (currencyEntities.isEmpty()) return@withContext false
        val localCurrencies = currencyEntities.map { LocalCurrency(it.id, it.name, it.symbol) }
        currenciesLocalDataSource.importCurrencies(localCurrencies)
        context.dataStore.edit { states ->
            states[booleanPreferencesKey(CURRENCY_LOADING_STATE)] = true
        }
        return@withContext true
    }

    companion object {
        private const val DATA_FILENAME = "currencies.json"
        private const val KEY_CURRENCY = "currencies"
        private const val KEY_ID = "id"
        private const val KEY_NAME = "name"
        private const val KEY_SYMBOL = "symbol"
        private const val CURRENCY_LOADING_STATE = "loading_state"
    }
}