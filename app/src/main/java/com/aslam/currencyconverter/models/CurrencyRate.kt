package com.aslam.currencyconverter.models

import android.util.Log

data class CurrencyRate(var from: String, var to: String, var value: Double, var rate: Double) {

    fun updateCurrencyRateList(currencyRatesMap: MutableMap<String, CurrencyRate>) {
        val cFrom = currencyRatesMap["USD$from"]?.rate
        val cTo = currencyRatesMap["USD$to"]?.rate
        if (cFrom != null && cTo != null) {
            rate = (cTo / cFrom) * value
        }
    }
}