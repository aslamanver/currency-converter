package com.aslam.currencyconverter

import android.content.Context
import com.aslam.currencyconverter.models.CurrencyRate
import com.aslam.currencyconverter.presenters.CEXPresenter
import com.aslam.currencyconverter.views.ICEXView
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.runners.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class UnitTest {

    @Mock
    private lateinit var mockContext: Context

    @Mock
    private lateinit var icexView: ICEXView

    @Test
    fun checkPresenterUSDLKR() {
        val cexPresenter = CEXPresenter(TestResponse.apiResponse, mockContext, icexView)
        val currencyRate = CurrencyRate("USD", "LKR", 1.0, 0.0)
        cexPresenter.updateCurrencyRate(currencyRate)
        assertTrue(currencyRate.rate == 196.69072)
    }

    @Test
    fun checkPresenterINRLKR() {
        val cexPresenter = CEXPresenter(TestResponse.apiResponse, mockContext, icexView)
        val currencyRate = CurrencyRate("INR", "LKR", 1.0, 0.0)
        cexPresenter.updateCurrencyRate(currencyRate)
        assertTrue(currencyRate.rate == 2.683288360098954)
    }
}