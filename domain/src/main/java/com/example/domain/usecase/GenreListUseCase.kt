package com.example.domain.usecase

import com.example.domain.model.response.GenreResponse
import com.example.domain.repository.MovieRepository
import io.reactivex.Single

class GenreListUseCase(private val movieRepository: MovieRepository) {
    fun execute(): Single<GenreResponse> {
        return movieRepository.getGenreList()
    }
}