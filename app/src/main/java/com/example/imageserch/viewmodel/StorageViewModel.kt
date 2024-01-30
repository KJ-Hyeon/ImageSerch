package com.example.imageserch.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.imageserch.data.SearchItem
import com.example.imageserch.repository.HomeRepository

class StorageViewModel(private val homeRepository: HomeRepository): ViewModel() {
    private var _likeList = MutableLiveData<List<SearchItem>>()
    var likeList: LiveData<List<SearchItem>> = _likeList

    fun getLikeList() {
        _likeList.value = homeRepository.loadLikeItems()
    }
    fun removeLikeItem(item: SearchItem) {
        homeRepository.removeItem(item)
    }

}