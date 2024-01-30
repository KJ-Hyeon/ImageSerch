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
    private var _likeList = MutableLiveData<List<SearchItem>>()
    var likeList: LiveData<List<SearchItem>> = _likeList

    fun getHomeData(key: String, query: String, page: Int = 1) {
        viewModelScope.launch {
            val image = homeRepository.getImageToHomeData(key, query, page)
            val video = homeRepository.getVideoToHomeData(key, query, page)
            (image + video).toMutableList().apply {
                sortByDescending { it.dateTime } // 날짜순으로 정렬
                checkLikeItems(this) // sp에 저장된 좋아요 이미지는 isLike를 true로 변경
            }.also {
                _searchList.value = it
                _isLoading.value = false
            }
        }
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

    private fun checkLikeItems(items: MutableList<SearchItem>){
        homeRepository.checkLikeItems(items)
    }

    fun updateCheckLikeItems() {
        searchList.value?.toMutableList()?.let {
            homeRepository.checkLikeItems(it)
        }
    }
}