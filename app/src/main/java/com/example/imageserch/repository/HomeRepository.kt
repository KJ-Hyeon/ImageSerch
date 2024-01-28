package com.example.imageserch.repository

import com.example.imageserch.data.HomeData
import com.example.imageserch.data.Image
import com.example.imageserch.data.ImageResponse
import com.example.imageserch.data.Video
import com.example.imageserch.network.RetrofitInstance
import com.example.imageserch.network.RetrofitInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.http.Query


class HomeRepository(private val retrofit: RetrofitInterface) {
    suspend fun getImage(key: String, query: String, page: Int) =
        withContext(Dispatchers.IO) { retrofit.getImage(key, query, page) }

    suspend fun getVideo(key: String, query: String, page: Int) =
        withContext(Dispatchers.IO) {retrofit.getVideo(key, query, page)}

    fun changeLikeState(item: HomeData): Boolean {
        if (item is Image) {
            val isLike = item.isLike
            item.isLike = !isLike
            return !isLike
        }
        if (item is Video) {
            val isLike = item.isLike
            item.isLike = !isLike
            return !isLike
        }
        return false
    }
}