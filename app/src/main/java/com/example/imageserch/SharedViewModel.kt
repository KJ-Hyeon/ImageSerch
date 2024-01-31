package com.example.imageserch

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.imageserch.data.SearchItem
import com.example.imageserch.repository.PreferenceRepository

class SharedViewModel(private val prefRepository: PreferenceRepository): ViewModel() {

    private var _likeList = MutableLiveData<List<SearchItem>>()
    var likeList: LiveData<List<SearchItem>> = _likeList

    fun getLikeList() {
        _likeList.value = prefRepository.loadLikeItems()
    }
    fun removeLikeItem(item: SearchItem) {
        prefRepository.removeItem(item)
    }

    fun addLikeItem(item: SearchItem) {
        prefRepository.addLikeItem(item)
    }

}