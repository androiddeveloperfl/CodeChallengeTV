package com.weathergroup.codechallengetv.data.util

import android.content.Context
import com.google.gson.Gson

class AssetsReader(private val context: Context, val gson: Gson) {
    fun getJsonDataFromAsset(context: Context = this.context, fileName: String): String {
        return context.assets.open(fileName).bufferedReader().use { it.readText() }
    }

    inline fun <reified T> readJsonFile(fileName: String): T? {
        val json = getJsonDataFromAsset(fileName = fileName)
        return gson.fromJson(json, T::class.java)
    }
}
