package com.example.data.repository

import com.example.data.api.ApiClient
import com.example.data.mapper.MovieMapper
import com.example.data.model.response.RemoteGenreResponse
import com.example.domain.model.response.GenreResponse
import com.example.domain.model.response.MovieResponse
import com.example.domain.model.response.ReviewResponse
import com.example.domain.model.response.VideoResponse
import com.example.domain.model.Movie
import com.example.domain.repository.MovieRepository
import io.reactivex.Single

class MovieRepositoryImpl(private val moviesMapper: MovieMapper)
    : MovieRepository {
    override fun getGenreList(
    ): Single<GenreResponse> {
        return ApiClient.movieService().getGenreList().map {
            moviesMapper.mapGenreListFromRemote(it)
        }
    }

    override fun getMoviesByGenre(page: Int, id: Int): Single<MovieResponse> {
        return ApiClient.movieService().getMoviesByGenre(page, id).map {
            moviesMapper.mapMovieGenreFromRemote(it)
        }
    }

    override fun getMovieById(id: Int): Single<Movie> {
        return ApiClient.movieService().getMovieById(id).map {
            moviesMapper.mapMovieByIdFromRemote(it)
        }
    }

    override fun getReviewByMovieId(id: Int, page: Int): Single<ReviewResponse> {
        return ApiClient.movieService().getReviewByMovieId(id, page).map {
            moviesMapper.mapReviewMovieFromRemote(it)

        }
    }

    override fun getVideoByMovieId(id: Int): Single<VideoResponse> {
        return ApiClient.movieService().getVideoByMovieId(id).map {
            moviesMapper.mapVideoMovieFromRemote(it)
        }
    }
}