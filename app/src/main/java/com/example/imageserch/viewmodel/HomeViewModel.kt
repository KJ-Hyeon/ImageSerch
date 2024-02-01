package com.example.imageserch.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.imageserch.MyApp
import com.example.imageserch.data.SearchItem

import com.example.imageserch.repository.HomeRepository
import com.example.imageserch.repository.PreferenceRepository

import kotlinx.coroutines.launch


class HomeViewModel(
    private val homeRepository: HomeRepository
) : ViewModel() {
    private var _searchList = MutableLiveData<List<SearchItem>>()
    var searchList: LiveData<List<SearchItem>> = _searchList
    private var _isLoading = MutableLiveData<Boolean>()
    var isLoading: LiveData<Boolean> = _isLoading
    private var _keywordList = MutableLiveData<List<String>>()
    var keywordList: LiveData<List<String>> = _keywordList
//    private var _page = MutableLiveData<Int>()
//    var page: LiveData<Int> = _page


    fun getHomeData(key: String, query: String, page: Int) {
        viewModelScope.launch {
            val searchList = homeRepository.getSearchItem(key, query, page)
            searchList.apply {
                checkLikeItems(this)
            }.also {
                _searchList.value = it
                _isLoading.value = false
            }
        }
    }

    private fun checkLikeItems(items: MutableList<SearchItem>) {
        homeRepository.checkLikeItems(items)
    }

    fun callSearchList() {
        searchList.value?.let {
            homeRepository.checkLikeItems(it.toMutableList())
            _searchList.value = it
        }
    }

    fun addKeywordList(item: String) {
        removeKeywordList(item)
        homeRepository.addKeyword(item)
        loadKeywordList()
//        val newKeywordList = _keywordList.value?.toMutableList()?.apply {
//            add(item)
//        } ?: mutableListOf()
//        _keywordList.value = newKeywordList
    }

    fun loadKeywordList() {
        _keywordList.value = homeRepository.loadKeywordList()
    }

    fun removeKeywordList(item: String) {
        homeRepository.removeKeyword(item)
        loadKeywordList()
//        val newKeywordList =
//            _keywordList.value?.toMutableList()?.apply {
//                remove(item)
//            } ?: mutableListOf()
//        _keywordList.value = newKeywordList
//        load하면 괜찮은가..
    }

}