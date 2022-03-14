package com.crypto.example

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.crypto.example.databinding.FragmentCurrencyListBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CurrencyListFragment : Fragment() {
    private val currencyListViewModel by activityViewModels<CurrencyListViewModel>()
    private lateinit var binding: FragmentCurrencyListBinding
    private lateinit var currencyList: java.util.ArrayList<CurrencyUiInfo>
    private lateinit var currencyListAdapter: CurrencyListAdapter
    private var clickedSort: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCurrencyListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.refreshLayout.setOnRefreshListener { currencyListViewModel.loadCurrencies() }
        initCurrencyListView()
    }

    private fun initCurrencyListView() {
        currencyListAdapter = CurrencyListAdapter(currencyListViewModel)
        binding.currencyListview.adapter = currencyListAdapter
        currencyList = requireArguments().getParcelableArrayList(KEY_CURRENCY_LIST) ?: ArrayList()
        currencyListAdapter.submitList(currencyList)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_demo, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_sort -> {
                if (clickedSort) return true
                clickedSort = true
                currencyListAdapter.submitList(currencyList.sortedBy { it.name })
                clickedSort = false
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    companion object {
        private const val KEY_CURRENCY_LIST = "currency_list"

        fun newInstance(currencies: ArrayList<CurrencyUiInfo>): CurrencyListFragment =
            CurrencyListFragment().apply {
                arguments = Bundle().apply {
                    putParcelableArrayList(KEY_CURRENCY_LIST, currencies)
                }
            }
    }
}