package com.crypto.example.data.database

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@SmallTest
class CurrencyDaoTest {
    private lateinit var currencyDatabase: CurrencyDatabase

    @get:Rule
    val instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        currencyDatabase = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            CurrencyDatabase::class.java
        ).allowMainThreadQueries().build()
        Dispatchers.setMain(StandardTestDispatcher())
    }

    @After
    fun tearDown() {
        currencyDatabase.close()
        Dispatchers.resetMain()
    }

    @Test
    fun testInsertAndGetCurrencyList() = runTest {
        val currencyList = listOf(
            LocalCurrency("1", "First", "FST"),
            LocalCurrency("2", "Second", "SND"),
            LocalCurrency("3", "Third", "TRD")
        )
        currencyDatabase.currencyDao().insertCurrencies(currencyList)
        val allCurrencies = currencyDatabase.currencyDao().loadAllCurrencies()

        assertThat(allCurrencies.size, `is`(currencyList.size))
    }
}