package com.example.imageserch.util

import android.content.Context
import android.util.Log
import com.example.imageserch.data.SearchItem
import com.google.gson.Gson

class PrefUtil(context: Context) {
    private val keywordPref = context.getSharedPreferences("keyword_pref", Context.MODE_PRIVATE)
    private val searchPref = context.getSharedPreferences("search_pref", Context.MODE_PRIVATE)

    fun getString(key: String, defaultValue: String): String {
        return keywordPref.getString(key,defaultValue).toString()
    }

    fun setString(key: String, value: String) {
        keywordPref.edit().putString(key, value).apply()
    }

    fun addLikeItem(item: SearchItem) {
        val jsonString = Gson().toJson(item)
        searchPref.edit().putString(item.thumbnail, jsonString).apply()
        Log.d("addLikeItem:","item.thumbnail: ${item.thumbnail}, $jsonString")
    }

    fun removeItem(item: SearchItem) {
        searchPref.edit().remove(item.thumbnail).apply()
        Log.d("removeItem:","removeItem: $item")
    }

    fun loadLikeItems(): MutableList<SearchItem> {
        val likeList = mutableListOf<SearchItem>()
        searchPref.all.forEach { key, value ->
            val likeItem = Gson().fromJson(value as? String, SearchItem::class.java)
            likeList.add(likeItem)
        }
        return likeList
    }


}