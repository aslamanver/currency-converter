package com.aslam.currencyconverter.views

import com.aslam.currencyconverter.models.AppData

interface ICEXView {
    fun onAPIDataAvailable(appData: AppData)
    fun onAPIError(ex: Exception)
}