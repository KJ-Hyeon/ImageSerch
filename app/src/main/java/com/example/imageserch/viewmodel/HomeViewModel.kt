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
    private var _isLoading = MutableLiveData<Boolean>()
    var isLoading: LiveData<Boolean> = _isLoading
    private var _page = MutableLiveData<Int>()
    var page: LiveData<Int> = _page


    fun getHomeData(key: String, query: String, page: Int) {
        viewModelScope.launch {
            val searchList = homeRepository.getSearchItem(key, query, page)
            searchList.apply {
                sortByDescending { it.dateTime }
                checkLikeItems(this)
            }.also {
                _searchList.value = it
                _isLoading.value = false
            }
        }
    }

    fun loadNextPage() {
        _page.value = page.value?.plus(1)
    }

    fun loadFirstPage() {
        _page.value = 1
    }


    fun addLikeItem(item: SearchItem) {
        homeRepository.addLikeItem(item)
    }

    fun removeLikeItem(item: SearchItem) {
        homeRepository.removeItem(item)
    }
//    fun loadLikeItems() {
//        _likeList.value = homeRepository.loadLikeItems()
//    }

    private fun checkLikeItems(items: MutableList<SearchItem>) {
        homeRepository.checkLikeItems(items)
    }


}