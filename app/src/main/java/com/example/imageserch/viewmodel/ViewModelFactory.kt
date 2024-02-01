package com.example.imageserch.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.imageserch.network.RetrofitInstance
import com.example.imageserch.repository.HomeRepository
import com.example.imageserch.repository.PreferenceRepository

class ViewModelFactory: ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            val repository = HomeRepository(RetrofitInstance.kakaoRetrofit)
            return HomeViewModel(repository) as T
        }
        if (modelClass.isAssignableFrom(SharedViewModel::class.java)) {
            val repository = PreferenceRepository()
            return SharedViewModel(repository) as T
        }
        else throw IllegalArgumentException ("UnKnown ViewModel $modelClass")
    }
}