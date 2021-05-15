package com.aslam.currencyconverter.presenters

import android.content.Context
import com.aslam.currencyconverter.models.AppData
import com.aslam.currencyconverter.models.CurrencyRate
import com.aslam.currencyconverter.utils.StorageHelper
import com.aslam.currencyconverter.views.ICEXView
import com.retrofit.lite.services.APITask
import org.json.JSONObject

class CEXPresenter(private var context: Context, private var icxView: ICEXView) : ICEXPresenter {

    private var currencyRatesMap = mutableMapOf<String, CurrencyRate>()

    override fun fetchAPIData() {

        APITask.from(context).sendGET(100, "http://api.currencylayer.com/live?access_key=5ba06a525d974428971140726013c7a3", null, object : APITask.Listener {

            override fun onSuccess(pid: Int, status: Int, headers: Map<String, String>, body: String) {

                val jsonResponse = JSONObject(body)
                val quotes = jsonResponse.getJSONObject("quotes")
                val from = jsonResponse.getString("source");
                val keys = quotes.names()

                for (i in 0 until keys.length()) {
                    val to = keys.getString(i).substring(3, 6)
                    val rate = quotes.getDouble(keys.getString(i))
                    currencyRatesMap[keys.getString(i)] = CurrencyRate(from, to, 1.0, rate)
                }

                val appData = AppData(currencyRatesMap)
                StorageHelper.storeAppData(context, appData)

                icxView.onAPIDataAvailable(appData)
            }

            override fun onFailed(pid: Int, ex: Exception) {
                icxView.onAPIError(ex)
            }
        })
    }
}