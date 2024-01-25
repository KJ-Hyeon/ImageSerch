package com.example.imageserch.data

import com.google.gson.annotations.SerializedName

data class ImageResponse(
    @SerializedName("documents")
    val images: List<Image>,
    @SerializedName("meta")
    val imageMeta: ImageMeta
)