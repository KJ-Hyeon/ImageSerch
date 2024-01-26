package com.example.imageserch

import android.app.Application
import android.content.Context
import com.example.imageserch.util.PrefUtil

class MyApp: Application() {
    companion object {
        lateinit var pref: PrefUtil
    }
    override fun onCreate() {
        pref = PrefUtil(applicationContext)
        super.onCreate()
    }
}