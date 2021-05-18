package com.aslam.currencyconverter

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View.OnFocusChangeListener
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.aslam.currencyconverter.adapters.CurrencyExchangeAdapter
import com.aslam.currencyconverter.databinding.ActivityMainBinding
import com.aslam.currencyconverter.models.CurrencyRate
import com.aslam.currencyconverter.presenters.CEXPresenter
import com.aslam.currencyconverter.views.ICEXView

class MainActivity : AppCompatActivity(), ICEXView {

    private lateinit var binding: ActivityMainBinding

    private lateinit var listViewAdapter: CurrencyExchangeAdapter
    private var listViewRateList: MutableList<CurrencyRate> = ArrayList()

    private lateinit var autoCompleteAdapter: ArrayAdapter<String>
    private var autoCompleteList: MutableList<String> = ArrayList()

    private lateinit var cexPresenter: CEXPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        cexPresenter = CEXPresenter(this, this)
        cexPresenter.fetchAPIData()
    }

    override fun initUI() {

        binding.editTextAmount.setText("1.0")

        autoCompleteAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, autoCompleteList)
        binding.autoCompleteCurrency.setAdapter(autoCompleteAdapter)

        listViewAdapter = CurrencyExchangeAdapter(listViewRateList)
        binding.listViewExchange.adapter = listViewAdapter

        binding.btnConvert.setOnClickListener {

            listViewRateList.forEach {
                it.value = getUserAmount()
                it.from = getUserCurrency()
                cexPresenter.updateCurrencyRate(it)
            }

            listViewAdapter.notifyDataSetChanged()
        }

        binding.editTextSearch.addTextChangedListener(object : TextWatcher {

            override fun onTextChanged(cs: CharSequence, s: Int, b: Int, c: Int) {

                listViewRateList.clear()

                cexPresenter.appData.currencyRatesMap.forEach {

                    if (it.value.to.contains(getUserQuery())) {
                        val element = CurrencyRate(getUserCurrency(), it.value.to, getUserAmount(), it.value.rate)
                        cexPresenter.updateCurrencyRate(element)
                        listViewRateList.add(element)
                    }
                }

                binding.textViewCount.text = listViewRateList.size.toString() + " Records"
                listViewAdapter.notifyDataSetChanged()
            }

            override fun afterTextChanged(editable: Editable) {}
            override fun beforeTextChanged(cs: CharSequence, i: Int, j: Int, k: Int) {}
        })

        binding.autoCompleteCurrency.onFocusChangeListener = OnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                val s = cexPresenter.appData.currencyRatesMap.filter { it.value.to == getUserCurrency() }.size
                if (s == 0) {
                    binding.autoCompleteCurrency.setText("USD")
                }
            }
        }
    }

    override fun getUserAmount(): Double {
        return binding.editTextAmount.text.toString().toDouble()
    }

    override fun getUserCurrency(): String {
        return binding.autoCompleteCurrency.text.toString().toUpperCase()
    }

    override fun getUserQuery(): String {
        return binding.editTextSearch.text.toString().toUpperCase()
    }

    override fun onAPIDataAvailable() {

        listViewRateList.clear()
        autoCompleteList.clear()

        cexPresenter.appData.currencyRatesMap.forEach {
            listViewRateList.add(it.value.copy())
            autoCompleteList.add(it.value.to)
        }

        listViewAdapter.notifyDataSetChanged()
        autoCompleteAdapter.notifyDataSetChanged()
        binding.textViewCount.text = listViewRateList.size.toString() + " Records"
    }

    override fun onAPIError(ex: Exception) {
        binding.textViewCount.text = ex.toString()
    }
}