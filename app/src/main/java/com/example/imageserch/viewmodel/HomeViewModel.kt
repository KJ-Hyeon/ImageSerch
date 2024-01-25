package com.example.imageserch.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.imageserch.data.Image
import com.example.imageserch.data.ImageResponse
import com.example.imageserch.repository.HomeRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeViewModel(private val homeRepository : HomeRepository): ViewModel() {
    private var _homeData = MutableLiveData<ImageResponse>()
    var homeData: LiveData<ImageResponse> = _homeData

    fun getImage(key: String, query: String) {
        viewModelScope.launch {
            _homeData.value = homeRepository.getImage(key, query)
        }
    }
}
