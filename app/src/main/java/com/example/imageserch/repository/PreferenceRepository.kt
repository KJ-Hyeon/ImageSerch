package com.example.imageserch.repository

import com.example.imageserch.MyApp
import com.example.imageserch.data.SearchItem

class PreferenceRepository {

    fun addLikeItem(item: SearchItem) {
        MyApp.pref.addLikeItem(item)
    }

    fun removeItem(item: SearchItem) {
        MyApp.pref.removeItem(item)
    }

    fun loadLikeItems(): MutableList<SearchItem> {
        return MyApp.pref.loadLikeItems()
    }

}