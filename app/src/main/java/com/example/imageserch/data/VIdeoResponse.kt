package com.example.imageserch.data

import com.google.gson.annotations.SerializedName

data class VIdeoResponse(
    @SerializedName("documents")
    val videos: List<Video>,
    @SerializedName("meta")
    val videoMeta: VideoMeta
)