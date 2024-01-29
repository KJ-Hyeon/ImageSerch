package com.example.imageserch.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.imageserch.data.SearchItem

import com.example.imageserch.repository.HomeRepository

import kotlinx.coroutines.launch


class HomeViewModel(private val homeRepository: HomeRepository) : ViewModel() {
    private var _searchList = MutableLiveData<List<SearchItem>>()
    var searchList: LiveData<List<SearchItem>> = _searchList
    private var _likeList = MutableLiveData<List<SearchItem>>()
    var likeList: LiveData<List<SearchItem>> = _likeList

    fun getHomeData(key: String, query: String, page: Int = 1) {
        viewModelScope.launch {
            val image = homeRepository.getImageToHomeData(key, query, page)
            val video = homeRepository.getVideoToHomeData(key, query, page)
            (image + video).toMutableList().apply {
                sortByDescending { it.dateTime }
                checkLikeItems()
                Log.d("ViewModel:","searchList:: $this")
            }.also { _searchList.value = it }
        }
    }

    fun addLikeItem(item: SearchItem) {
        homeRepository.addLikeItem(item)
    }

    fun removeLikeItem(item: SearchItem) {
        homeRepository.removeItem(item)
    }
    fun loadLikeItems() {
        _likeList.value = homeRepository.loadLikeItems()
    }

    private fun MutableList<SearchItem>.checkLikeItems(){
        homeRepository.checkLikeItems(this)
    }
}