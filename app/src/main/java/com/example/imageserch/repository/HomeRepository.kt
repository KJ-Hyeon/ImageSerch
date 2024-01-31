package com.example.imageserch.repository

import com.example.imageserch.MyApp
import com.example.imageserch.data.SearchItem
import com.example.imageserch.network.RetrofitInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class HomeRepository(private val retrofit: RetrofitInterface) {

    private var searchList = mutableListOf<SearchItem>()

    private suspend fun getImage(key: String, query: String, page: Int) =
        withContext(Dispatchers.IO) { retrofit.getImage(key, query, page) }

    private suspend fun getVideo(key: String, query: String, page: Int) =
        withContext(Dispatchers.IO) {retrofit.getVideo(key, query, page)}

//    suspend fun getImageToHomeData(key: String, query: String, page: Int):MutableList<SearchItem> {
//        val searchImageList = mutableListOf<SearchItem>()
//        val imageResponse = getImage(key,query,page)
//        imageResponse.images.forEach {
//            val searchItem = SearchItem("image", it.thumbnail_url, it.display_sitename, it.datetime, it.display_sitename,false)
//            searchImageList.add(searchItem)
//        }
//        return searchImageList
//    }

    private suspend fun getApiToSearchItem (key: String, query: String, page: Int):MutableList<SearchItem> {
        val searchItems = mutableListOf<SearchItem>()
        val videoResponse = getVideo(key, query, page)
        val imageResponse = getImage(key, query, page)

        searchItems.addAll(videoResponse.videos.map {
            SearchItem("video", it.thumbnail, it.title, it.datetime, it.title, false)
        })

        searchItems.addAll(imageResponse.images.map {
            SearchItem("image", it.thumbnail_url, it.display_sitename, it.datetime, it.display_sitename, false)
        })

        return searchItems
    }

    suspend fun getSearchItem(key: String, query: String, page: Int): MutableList<SearchItem> {
        val newSearchList = getApiToSearchItem(key, query, page)
        if(isNewSearch(page)) searchList.clear()
        return searchList.apply { addAll(newSearchList) }
    }

    private fun isNewSearch(page: Int) = page==1

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