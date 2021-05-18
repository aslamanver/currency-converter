package com.aslam.currencyconverter.presenters

import android.content.Context
import android.util.Log
import com.aslam.currencyconverter.models.AppData
import com.aslam.currencyconverter.models.CurrencyRate
import com.aslam.currencyconverter.utils.StorageHelper
import com.aslam.currencyconverter.views.ICEXView
import com.retrofit.lite.services.APITask
import org.json.JSONObject
import java.util.*

class CEXPresenter(private var context: Context, private var icxView: ICEXView) : ICEXPresenter {

    var appData: AppData

    init {
        appData = StorageHelper.getAppData(context)
        icxView.initUI()
    }

    fun updateCurrencyRate(currencyRate: CurrencyRate) {
        val cFrom = appData.currencyRatesMap["USD${currencyRate.from}"]?.rate
        val cTo = appData.currencyRatesMap["USD${currencyRate.to}"]?.rate
        if (cFrom != null && cTo != null) {
            currencyRate.rate = (cTo / cFrom) * currencyRate.value
        }
    }

    override fun fetchAPIData() {

        if (appData.currencyRatesMap.isNotEmpty()) {
            val syncAgo: Long = (Calendar.getInstance().time.time - appData.syncTime.time) / 1000
            if (syncAgo <= (60 * 30)) {
                icxView.onAPIDataAvailable()
                return
            }
        }

        APITask.from(context).sendGET(100, "http://api.currencylayer.com/live?access_key=5ba06a525d974428971140726013c7a3", null, object : APITask.Listener {

            override fun onSuccess(pid: Int, status: Int, headers: Map<String, String>, body: String) {

                val jsonResponse = JSONObject(body)
                val quotes = jsonResponse.getJSONObject("quotes")
                val from = jsonResponse.getString("source");
                val keys = quotes.names()

                for (i in 0 until keys.length()) {

                    val to = keys.getString(i).substring(3, 6)
                    val rate = quotes.getDouble(keys.getString(i))

                    appData.currencyRatesMap[keys.getString(i)] = CurrencyRate(from, to, 1.0, rate)
                }

                appData = AppData(appData.currencyRatesMap)
                StorageHelper.storeAppData(context, appData)

                icxView.onAPIDataAvailable()
            }

            override fun onFailed(pid: Int, ex: Exception) {
                icxView.onAPIError(ex)
            }
        })
    }
}