package com.aslam.currencyconverter

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View.OnFocusChangeListener
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.aslam.currencyconverter.adapters.CurrencyExchangeAdapter
import com.aslam.currencyconverter.databinding.ActivityMainBinding
import com.aslam.currencyconverter.models.AppData
import com.aslam.currencyconverter.models.CurrencyRate
import com.aslam.currencyconverter.presenters.CEXPresenter
import com.aslam.currencyconverter.utils.StorageHelper
import com.aslam.currencyconverter.views.ICEXView
import com.retrofit.lite.services.APITask
import org.json.JSONObject

class MainActivity : AppCompatActivity(), ICEXView {

    private lateinit var binding: ActivityMainBinding
    private var currencyRatesMap = mutableMapOf<String, CurrencyRate>()
    private lateinit var listViewAdapter: CurrencyExchangeAdapter
    private var listViewRateList: MutableList<CurrencyRate> = ArrayList()
    private lateinit var autoCompleteAdapter: ArrayAdapter<*>
    private var autoCompleteList: MutableList<String> = ArrayList()

    private lateinit var cexPresenter: CEXPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        cexPresenter = CEXPresenter(this, this);

        binding.editTextAmount.setText("1.0")

        autoCompleteAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, autoCompleteList)
        binding.autoCompleteCurrency.setAdapter(autoCompleteAdapter)

        listViewAdapter = CurrencyExchangeAdapter(this, listViewRateList)
        binding.listViewExchange.adapter = listViewAdapter

        retrieveData()

        binding.btnConvert.setOnClickListener {

            var amount = binding.editTextAmount.text.toString().toDouble()
            var currency = binding.autoCompleteCurrency.text.toString()

            listViewRateList.forEach {
                it.value = amount
                it.from = currency
                it.updateCurrencyRateList(currencyRatesMap)
            }

            listViewAdapter.notifyDataSetChanged()
        }

        binding.editTextSearch.addTextChangedListener(object : TextWatcher {

            override fun onTextChanged(cs: CharSequence, s: Int, b: Int, c: Int) {

                listViewRateList.clear()
                currencyRatesMap.forEach {

                    if (it.value.to.contains(binding.editTextSearch.text.toString().toUpperCase())) {

                        var amount = binding.editTextAmount.text.toString().toDouble()
                        var currency = binding.autoCompleteCurrency.text.toString()
                        val element = CurrencyRate(currency, it.value.to, amount, it.value.rate)
                        element.updateCurrencyRateList(currencyRatesMap)
                        listViewRateList.add(element)
                    }
                }

                binding.textViewCount.text = listViewRateList.size.toString() + " Records"
                listViewAdapter.notifyDataSetChanged()
            }

            override fun afterTextChanged(editable: Editable) {}
            override fun beforeTextChanged(cs: CharSequence, i: Int, j: Int, k: Int) {}
        })

        binding.autoCompleteCurrency.onFocusChangeListener = OnFocusChangeListener { v, hasFocus ->

            if (!hasFocus) {

                val s = currencyRatesMap.filter {
                    it.value.to.contains(binding.autoCompleteCurrency.text.toString().toUpperCase())
                }.size

                if (s <= 0) {
                    binding.autoCompleteCurrency.setText("USD");
                }
            }
        }
    }

    private fun retrieveData() {

        APITask.from(this).sendGET(100, "http://api.currencylayer.com/live?access_key=5ba06a525d974428971140726013c7a3", null, object : APITask.Listener {

            override fun onSuccess(pid: Int, status: Int, headers: Map<String, String>, body: String) {

                currencyRatesMap.clear()
                listViewRateList.clear()
                autoCompleteList.clear()

                val jsonResponse = JSONObject(body)
                val quotes = jsonResponse.getJSONObject("quotes")
                val from = jsonResponse.getString("source");
                val keys = quotes.names()

                for (i in 0 until keys.length()) {
                    val to = keys.getString(i).substring(3, 6)
                    val rate = quotes.getDouble(keys.getString(i))

                    currencyRatesMap[keys.getString(i)] = CurrencyRate(from, to, 1.0, rate)
                }

                // StorageHelper.storeAppData(applicationContext, AppData(currencyRatesMap))
                // val appData = StorageHelper.getAppData(applicationContext)

                currencyRatesMap.forEach {
                    listViewRateList.add(it.value.copy())
                    autoCompleteList.add(it.value.to)
                }

                listViewAdapter.notifyDataSetChanged()
                autoCompleteAdapter.notifyDataSetChanged()
                binding.textViewCount.text = listViewRateList.size.toString() + " Records"
            }

            override fun onFailed(pid: Int, ex: Exception) {
                Log.e("SOME", ex.toString())
            }
        })
    }

    override fun onAPIDataAvailable(appData: AppData) {
        TODO("Not yet implemented")
    }

    override fun onAPIError(ex: Exception) {
        TODO("Not yet implemented")
    }
}