package com.crypto.example

import android.content.Context
import androidx.lifecycle.*
import com.crypto.example.domain.repositories.Error
import com.crypto.example.domain.repositories.Success
import com.crypto.example.domain.usecases.CheckImportStateUseCase
import com.crypto.example.domain.usecases.LoadCurrenciesUseCase
import com.crypto.example.domain.usecases.ImportCurrenciesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CurrencyListViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val loadCurrenciesUseCase: LoadCurrenciesUseCase,
    private val importCurrenciesUseCase: ImportCurrenciesUseCase,
    private val checkImportStateUseCase: CheckImportStateUseCase
) : ViewModel() {
    private val _currenciesState: MutableStateFlow<CurrencyListState> =
        MutableStateFlow(CurrencyListState())
    val currenciesState: StateFlow<CurrencyListState> = _currenciesState.asStateFlow()

    private val _selectedCurrency: MutableLiveData<CurrencyUiInfo> = MutableLiveData()
    val selectedCurrency: LiveData<CurrencyUiInfo> = _selectedCurrency

    private var fetchCurrencyListJob: Job? = null

    suspend fun importData(context: Context): Boolean =
        if (checkImportStateUseCase(context) || savedStateHandle.get<Boolean>(STATE_IMPORT_DATA) == true) {
            true
        } else {
            val importStatus = importCurrenciesUseCase(context)
            savedStateHandle[STATE_IMPORT_DATA] = importStatus
            importStatus
        }

    fun loadCurrencies() {
        fetchCurrencyListJob?.cancel()
        fetchCurrencyListJob = viewModelScope.launch {
            _currenciesState.update {
                it.copy(isFetchingCurrencies = true)
            }
            when (val dataResult = loadCurrenciesUseCase()) {
                is Success -> {
                    _currenciesState.update {
                        it.copy(
                            isFetchingCurrencies = false,
                            currencyList = dataResult.values.map(CurrencyUiInfo.Companion::fromEntity)
                        )
                    }
                }
                is Error -> {
                    _currenciesState.update {
                        it.copy(
                            isFetchingCurrencies = false,
                            errorMessage = getErrorMessage(dataResult.exception)
                        )
                    }
                }
            }
        }
    }

    private fun getErrorMessage(exception: Exception) = exception.message ?: DEFAULT_ERROR_MESSAGE

    fun selectCurrency(currencyUiInfo: CurrencyUiInfo) {
        _selectedCurrency.value = currencyUiInfo
    }

    companion object {
        private const val DEFAULT_ERROR_MESSAGE = "An error occurred!"
        private const val STATE_IMPORT_DATA = "state_import_data"
    }
}