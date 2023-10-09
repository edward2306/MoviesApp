package com.example.domain.model.response

import com.example.domain.model.Movie

data class MovieResponse(
    val page: Int?,
    val total_results: Int?,
    val total_pages: Int?,
    val results: List<Movie>
)