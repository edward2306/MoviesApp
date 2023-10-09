package com.example.domain.usecase

import com.example.domain.model.response.MovieResponse
import com.example.domain.repository.MovieRepository
import io.reactivex.Single

class MovieGenreUseCase(private val movieRepository: MovieRepository) {
    fun execute(id: Int, page: Int): Single<MovieResponse> {
        return movieRepository.getMoviesByGenre(page, id)
    }
}