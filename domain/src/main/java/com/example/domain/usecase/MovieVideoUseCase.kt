package com.example.domain.usecase

import com.example.domain.model.response.VideoResponse
import com.example.domain.repository.MovieRepository
import io.reactivex.Single

class MovieVideoUseCase(private val movieRepository: MovieRepository) {
    fun execute(id: Int): Single<VideoResponse> {
        return movieRepository.getVideoByMovieId(id)
    }
}