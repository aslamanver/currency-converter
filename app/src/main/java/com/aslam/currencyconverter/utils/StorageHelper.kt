package com.aslam.currencyconverter.utils

import android.content.Context
import com.aslam.currencyconverter.BuildConfig
import com.aslam.currencyconverter.models.AppData
import com.google.gson.Gson
import com.google.gson.GsonBuilder


class StorageHelper {

    companion object {

        private const val PREF: String = BuildConfig.APPLICATION_ID + ".PREF"

        private fun getGson(): Gson? {
            return GsonBuilder().create()
        }

        fun storeAppData(context: Context, appData: AppData) {
            val editor = context.getSharedPreferences(PREF, Context.MODE_PRIVATE).edit()
            val json = getGson()?.toJson(appData)
            editor.putString("APP_DATA", json)
            editor.commit()
        }

        fun getAppData(context: Context): AppData {
            val json = context.getSharedPreferences(PREF, Context.MODE_PRIVATE).getString("APP_DATA", null)
            return if (json != null) getGson()?.fromJson(json, AppData::class.java)!! else AppData(mutableMapOf())
        }
    }
}