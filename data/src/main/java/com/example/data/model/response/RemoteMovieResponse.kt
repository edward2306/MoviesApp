package com.example.data.model.response

import com.example.domain.model.Movie
import com.google.gson.annotations.SerializedName

data class RemoteMovieResponse(
    @SerializedName("page") var page: Int? = null,
    @SerializedName("total_results") var total_results: Int? = null,
    @SerializedName("total_pages") var total_pages: Int? = null,
    @SerializedName("results") var results: List<Movie>? = null
)