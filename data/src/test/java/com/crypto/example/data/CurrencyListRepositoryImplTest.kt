package com.crypto.example.data

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.crypto.example.data.database.LocalCurrency
import com.crypto.example.domain.repositories.Success
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule
import org.mockito.kotlin.whenever

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@SmallTest
class CurrencyListRepositoryImplTest {

    @get:Rule
    val mockitoRule: MockitoRule = MockitoJUnit.rule()

    @get:Rule
    val instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var mockCurrencyListLocalDataSource: CurrencyListLocalDataSource

    lateinit var currencyListRepositoryImpl: CurrencyListRepositoryImpl

    private val fakeCurrencyList = listOf(
        LocalCurrency("1", "First", "FST"),
        LocalCurrency("2", "Second", "SND"),
        LocalCurrency("3", "Third", "TRD")
    )

    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        currencyListRepositoryImpl =
            CurrencyListRepositoryImpl(mockCurrencyListLocalDataSource, testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun test_getAllCurrencies_Success() = runTest {
        whenever(mockCurrencyListLocalDataSource.loadAllCurrencies()).thenReturn(fakeCurrencyList)
        val dataResult = currencyListRepositoryImpl.loadAllCurrencies()
        assertThat(dataResult is Success, `is`(true))
        assertThat((dataResult as Success).values.size, `is`(fakeCurrencyList.size))
    }

}