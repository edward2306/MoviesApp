package com.example.domain.usecase

import com.example.domain.model.response.ReviewResponse
import com.example.domain.repository.MovieRepository
import io.reactivex.Single

class MovieReviewUseCase(private val movieRepository: MovieRepository) {
    fun execute(id: Int, page: Int): Single<ReviewResponse> {
        return movieRepository.getReviewByMovieId(id, page)
    }
}