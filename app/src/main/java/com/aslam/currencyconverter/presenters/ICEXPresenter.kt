package com.aslam.currencyconverter.presenters

import com.aslam.currencyconverter.models.AppData

interface ICEXPresenter {
    fun fetchAPIData()
    fun prepareAPIData(body: String): AppData
}