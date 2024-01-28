package com.example.imageserch.data

data class Video(
    val author: String,
    val datetime: String,
    val play_time: Int,
    val thumbnail: String,
    val title: String,
    val url: String,
    var isLike: Boolean = false
): HomeData()