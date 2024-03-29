package com.example.imageserch.network

import com.example.imageserch.data.ImageResponse
import com.example.imageserch.data.VIdeoResponse
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface RetrofitInterface {
    @GET("v2/search/image")
    suspend fun getImage(
        @Header ("Authorization") key: String,
        @Query ("query") query: String,
        @Query ("page") page: Int,
        @Query ("size") size: Int = 50
    ): ImageResponse

    @GET("v2/search/vclip")
    suspend fun getVideo(
        @Header ("Authorization") key: String,
        @Query ("query") query: String,
        @Query ("page") page: Int,
        @Query ("size") size: Int = 30
    ): VIdeoResponse

}