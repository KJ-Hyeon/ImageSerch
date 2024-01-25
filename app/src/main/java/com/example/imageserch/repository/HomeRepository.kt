package com.example.imageserch.repository

import com.example.imageserch.data.ImageResponse
import com.example.imageserch.network.RetrofitInstance
import com.example.imageserch.network.RetrofitInterface
import retrofit2.http.Query

//class HomeRepository(private val retrofit: RetrofitInterface) {
//    suspend fun getImage(key: String, query: String): ImageResponse {
//        return retrofit.getImage(key, query)
//    }
//
//}
class HomeRepository(private val retrofit: RetrofitInterface) {
    suspend fun getImage(key: String, query: String): ImageResponse {
        return retrofit.getImage(key, query)
    }
}