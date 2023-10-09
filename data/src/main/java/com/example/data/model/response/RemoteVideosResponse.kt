package com.example.data.model.response

import com.example.domain.model.Video
import com.google.gson.annotations.SerializedName

data class RemoteVideosResponse(
    @SerializedName("id") var id: Int? = 0,
    @SerializedName("results") var results: List<Video>? = null
)