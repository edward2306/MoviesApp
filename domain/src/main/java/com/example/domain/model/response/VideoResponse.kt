package com.example.domain.model.response

import com.example.domain.model.Video

data class VideoResponse(
    val id: Int,
    val results: List<Video>?
)