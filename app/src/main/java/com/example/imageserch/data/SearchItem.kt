package com.example.imageserch.data

data class SearchItem(
    val type: String,
    val thumbnail: String,
    val title: String,
    val dateTime: String,
    val site: String,
    var like: Boolean = false
)