package com.example.imageserch.repository

import com.example.imageserch.data.ImageResponse
import com.example.imageserch.network.RetrofitInstance
import com.example.imageserch.network.RetrofitInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.http.Query


class HomeRepository(private val retrofit: RetrofitInterface) {
    suspend fun getImage(key: String, query: String, page: Int) =
        withContext(Dispatchers.IO) { retrofit.getImage(key, query, page) }
}