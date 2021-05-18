package com.aslam.currencyconverter.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.aslam.currencyconverter.databinding.RateListRowBinding
import com.aslam.currencyconverter.models.CurrencyRate

class CurrencyExchangeAdapter(private var sourceRateList: MutableList<CurrencyRate> = ArrayList()) : BaseAdapter() {

    override fun getCount(): Int {
        return sourceRateList.size
    }

    override fun getItem(position: Int): Any {
        return sourceRateList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val layoutInflater = LayoutInflater.from(parent!!.context)
        val binding: RateListRowBinding = RateListRowBinding.inflate(layoutInflater, parent, false)
        binding.textViewTitle.text =
            sourceRateList[position].value.toString() + " " +
                    sourceRateList[position].from + " = " +
                    sourceRateList[position].to + " " +
                    sourceRateList[position].rate.toString()
        return binding.root
    }
}