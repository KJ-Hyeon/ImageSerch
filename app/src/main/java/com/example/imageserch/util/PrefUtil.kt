package com.example.imageserch.util

import android.content.Context

class PrefUtil(context: Context) {
    private val pref = context.getSharedPreferences("image_pref", Context.MODE_PRIVATE)

    fun getString(key: String, defaultValue: String): String {
        return pref.getString(key,defaultValue).toString()
    }

    fun setString(key: String, value: String) {
        pref.edit().putString(key, value).apply()
    }

}