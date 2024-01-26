package com.example.imageserch.network

import com.example.imageserch.data.ImageResponse
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface RetrofitInterface {
    @GET("v2/search/image")
    suspend fun getImage(
        @Header ("Authorization") key: String,
        @Query ("query") query: String,
        @Query ("page") page: Int
    ): ImageResponse

}