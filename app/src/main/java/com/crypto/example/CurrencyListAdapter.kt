package com.crypto.example

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.DiffUtil.ItemCallback
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.crypto.example.databinding.ItemCurrencyBinding

class CurrencyListAdapter(private val currencyListViewModel: CurrencyListViewModel): ListAdapter<CurrencyUiInfo, CurrencyListAdapter.CurrencyViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrencyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemCurrencyBinding.inflate(inflater, parent, false)
        return CurrencyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CurrencyViewHolder, position: Int) {
        val currency = getItem(position)
        holder.bind(currency, currencyListViewModel)
    }

    class CurrencyViewHolder(private val binding: ItemCurrencyBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(currency: CurrencyUiInfo, currencyListViewModel: CurrencyListViewModel) {
            itemView.setOnClickListener {
                currencyListViewModel.selectCurrency(currency)
            }
            binding.textIcon.text = currency.name.trim().first().toString()
            binding.textName.text = currency.name.trim()
            binding.textSymbol.text = currency.symbol.trim()
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : ItemCallback<CurrencyUiInfo>() {
            override fun areItemsTheSame(
                oldItem: CurrencyUiInfo,
                newItem: CurrencyUiInfo
            ): Boolean = newItem.id == oldItem.id

            override fun areContentsTheSame(
                oldItem: CurrencyUiInfo,
                newItem: CurrencyUiInfo
            ): Boolean = newItem.name == oldItem.name && newItem.symbol == oldItem.symbol
        }
    }
}