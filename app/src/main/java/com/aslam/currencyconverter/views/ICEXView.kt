package com.aslam.currencyconverter.views

interface ICEXView {
    fun initUI()
    fun onAPIDataAvailable()
    fun onAPIError(ex: Exception)
    fun getUserAmount(): Double
    fun getUserCurrency(): String
    fun getUserQuery(): String
}