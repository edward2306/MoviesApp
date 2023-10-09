package com.example.domain.repository;

import com.example.domain.model.Movie
import com.example.domain.model.response.GenreResponse
import com.example.domain.model.response.MovieResponse
import com.example.domain.model.response.ReviewResponse
import com.example.domain.model.response.VideoResponse
import io.reactivex.Single

interface MovieRepository {

    fun getGenreList(): Single<GenreResponse>

    fun getMoviesByGenre(page: Int, id: Int): Single<MovieResponse>

    fun getMovieById(id: Int): Single<Movie>

    fun getReviewByMovieId(id: Int, page: Int): Single<ReviewResponse>

    fun getVideoByMovieId(id: Int): Single<VideoResponse>

}