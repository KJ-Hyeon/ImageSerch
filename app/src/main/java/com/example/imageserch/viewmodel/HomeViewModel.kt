package com.example.imageserch.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.imageserch.data.HomeData
import com.example.imageserch.data.Image
import com.example.imageserch.data.ImageResponse
import com.example.imageserch.data.Video
import com.example.imageserch.repository.HomeRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class HomeViewModel(private val homeRepository: HomeRepository) : ViewModel() {
    private var _homeData = MutableLiveData<List<HomeData>>()
    var homeData: LiveData<List<HomeData>> = _homeData
    private var _isLike = MutableLiveData<Boolean>()
    var isLike: LiveData<Boolean> = _isLike

//    fun getImage(key: String, query: String, page: Int = 1) {
//        viewModelScope.launch {
//            _homeData.value = homeRepository.getImage(key, query, page)
//        }
//    }

    fun getHomeData(key: String, query: String, page: Int = 1) {
        viewModelScope.launch {
            val image = homeRepository.getImage(key, query, page)
            val video = homeRepository.getVideo(key, query, page)
            val homList = image.images + video.videos
            _homeData.value = homList
        }
    }
    fun changeLikeState(item: HomeData): Boolean = homeRepository.changeLikeState(item)

//    fun changeLikeState(item: HomeData) {
//        _isLike.value = homeRepository.changeLikeState(item)
//    }


}