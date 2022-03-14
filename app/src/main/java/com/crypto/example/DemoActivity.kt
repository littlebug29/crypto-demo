package com.crypto.example

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.commit
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.crypto.example.databinding.ActivityDemoBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DemoActivity : AppCompatActivity() {
    private val currencyListViewModel: CurrencyListViewModel by viewModels()
    private lateinit var binding: ActivityDemoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDemoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        binding.buttonLoad.setOnClickListener { currencyListViewModel.loadCurrencies() }
        initObservers()
        importDataIntoDatabase()
    }

    private fun initObservers() {
        currencyListViewModel.selectedCurrency.observe(this) {
            Toast.makeText(this, "Selected ${it.name}", Toast.LENGTH_SHORT).show()
        }
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                currencyListViewModel.currenciesState
                    .collect { state ->
                        toggleLoadingState(state.isFetchingCurrencies)
                        binding.buttonLoad.isVisible = state.canShowLoadButton
                        if (state.errorMessage != null) {
                            showErrorMessage(state.errorMessage)
                            return@collect
                        }
                        val currencyList = state.currencyList
                        binding.container.isVisible = currencyList.isNotEmpty()
                        if (currencyList.isNotEmpty()) {
                            showCurrencyData(currencyList)
                        }
                    }
            }
        }
    }

    private fun importDataIntoDatabase() = lifecycleScope.launch {
        currencyListViewModel.importData(applicationContext)
    }

    private fun toggleLoadingState(isLoading: Boolean) {
        binding.progressCircular.isVisible = isLoading
    }

    private fun showErrorMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun showCurrencyData(currencies: List<CurrencyUiInfo>) {
        supportFragmentManager.commit {
            setReorderingAllowed(true)
            replace(R.id.container, CurrencyListFragment.newInstance(ArrayList(currencies)))
        }
    }
}