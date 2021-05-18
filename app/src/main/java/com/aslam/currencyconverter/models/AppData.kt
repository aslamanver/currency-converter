package com.aslam.currencyconverter.models

import java.util.*

class AppData(var currencyRatesMap: MutableMap<String, CurrencyRate>, var syncTime: Date = Calendar.getInstance().time)