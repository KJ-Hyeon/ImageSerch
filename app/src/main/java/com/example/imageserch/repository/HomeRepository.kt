package com.example.imageserch.repository

import android.util.Log
import com.example.imageserch.MyApp
import com.example.imageserch.data.SearchItem
import com.example.imageserch.network.RetrofitInterface
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException


class HomeRepository(private val retrofit: RetrofitInterface) {

    private var searchList = mutableListOf<SearchItem>()
    private val pageExceptionHandler = CoroutineExceptionHandler { _, exception ->
        Log.e("pageExceptionHandler","$exception")
    }

    private suspend fun getImage(key: String, query: String, page: Int) =
        withContext(Dispatchers.IO) {
            runCatching {
                retrofit.getImage(key, query, page,)
            }.onFailure {
                Log.e("getImageError:","$it")
            }
        }


    private suspend fun getVideo(key: String, query: String, page: Int) =
        withContext(Dispatchers.IO) {
            runCatching {
                retrofit.getVideo(key, query, page)
            }.onFailure {
                Log.e("getVideoError:","$it")
            }
        }

    private suspend fun getApiToSearchItem (key: String, query: String, page: Int):MutableList<SearchItem> {
        val searchItems = mutableListOf<SearchItem>()
        getVideo(key, query, page)?.let {result ->
            result.onSuccess { videoResponse ->
                searchItems.addAll(videoResponse.videos.map {
                SearchItem("video", it.thumbnail, it.title, it.datetime, it.title, false)
            })}
        }
        getImage(key, query, page).let {result->
            result.onSuccess { imageResponse->
                searchItems.addAll(imageResponse.images.map {
                    SearchItem("image", it.thumbnail_url, it.display_sitename, it.datetime, it.display_sitename, false)
                })
            }
        }
        return searchItems
    }

    suspend fun getSearchItem(key: String, query: String, page: Int): MutableList<SearchItem> {
        val newSearchList = getApiToSearchItem(key, query, page)
        if(isNewSearch(page)) searchList.clear()
        return searchList.apply { addAll(newSearchList) }
    }

    private fun isNewSearch(page: Int) = page==1

    fun checkLikeItems(items: MutableList<SearchItem>) {
        val likeList = MyApp.pref.loadLikeItems()
        val likeKeyList = likeList.map { it.thumbnail }
        items.forEach { searchItem ->
           if (searchItem.thumbnail in likeKeyList) searchItem.like = true
        }
    }
}