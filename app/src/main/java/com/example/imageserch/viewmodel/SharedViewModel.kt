package com.example.imageserch.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.imageserch.data.SearchItem
import com.example.imageserch.repository.PreferenceRepository

class SharedViewModel(private val prefRepository: PreferenceRepository): ViewModel() {

    private var _likeList = MutableLiveData<List<SearchItem>>()
    var likeList: LiveData<List<SearchItem>> = _likeList
    private var _isBadgeVisible = MutableLiveData<Boolean> ()
    var isBadgeVisible: LiveData<Boolean>  = _isBadgeVisible

    fun getLikeList() {
        _likeList.value = prefRepository.loadLikeItems()
    }
    fun removeLikeItem(item: SearchItem) {
        prefRepository.removeItem(item)
        if (prefRepository.loadLikeItems().isEmpty()) _isBadgeVisible.value = false
    }

    fun addLikeItem(item: SearchItem) {
        // add를 할 때 뱃지변수를 true
        prefRepository.addLikeItem(item)
        _isBadgeVisible.value = true
    }

    fun setBadgeInvisible() {
        _isBadgeVisible.value = false
    }

}