package com.example.domain.model.response

import com.example.domain.model.Review

data class ReviewResponse(
    val page: Int?,
    val total_results: Int?,
    val total_pages: Int?,
    val results: List<Review>?
)