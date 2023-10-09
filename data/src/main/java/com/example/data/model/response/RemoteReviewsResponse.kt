package com.example.data.model.response

import com.example.domain.model.Review
import com.google.gson.annotations.SerializedName

data class RemoteReviewsResponse(
    @SerializedName("page") var page: Int? = null,
    @SerializedName("total_results") var total_results: Int? = null,
    @SerializedName("total_pages") var total_pages: Int? = null,
    @SerializedName("results") var results: List<Review>? = null
)