package com.example.imageserch.repository

import android.util.Log
import com.example.imageserch.MyApp
import com.example.imageserch.data.Image
import com.example.imageserch.data.ImageResponse
import com.example.imageserch.data.SearchItem
import com.example.imageserch.data.Video
import com.example.imageserch.network.RetrofitInstance
import com.example.imageserch.network.RetrofitInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.http.Query


class HomeRepository(private val retrofit: RetrofitInterface) {
    private suspend fun getImage(key: String, query: String, page: Int) =
        withContext(Dispatchers.IO) { retrofit.getImage(key, query, page) }

    private suspend fun getVideo(key: String, query: String, page: Int) =
        withContext(Dispatchers.IO) {retrofit.getVideo(key, query, page)}

    suspend fun getImageToHomeData(key: String, query: String, page: Int):MutableList<SearchItem> {
        val searchImageList = mutableListOf<SearchItem>()
        val imageResponse = getImage(key,query,page)
        imageResponse.images.forEach {
            val searchItem = SearchItem("image", it.thumbnail_url, it.display_sitename, it.datetime, it.display_sitename,false)
            searchImageList.add(searchItem)
        }
        return searchImageList
    }

    suspend fun getVideoToHomeData(key: String, query: String, page: Int):MutableList<SearchItem> {
        val searchVideoList = mutableListOf<SearchItem>()
        val videoResponse = getVideo(key,query,page)
        videoResponse.videos.forEach {
            val searchItem = SearchItem("video", it.thumbnail, it.title, it.datetime, it.title, false)
            searchVideoList.add(searchItem)
        }
        return searchVideoList
    }

    fun addLikeItem(item: SearchItem) {
        MyApp.pref.addLikeItem(item)
    }

    fun removeItem(item: SearchItem) {
        MyApp.pref.removeItem(item)
    }

    fun loadLikeItems(): MutableList<SearchItem> {
        return MyApp.pref.loadLikeItems()
    }

    fun checkLikeItems(items: MutableList<SearchItem>) {
        val likeKeyList = loadLikeItems().map { it.thumbnail }
        items.forEach { searchItem ->
           if (searchItem.thumbnail in likeKeyList) searchItem.like = true
        }
    }
}