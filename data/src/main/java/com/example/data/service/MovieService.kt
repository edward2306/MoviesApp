package com.example.data.service

import com.example.data.model.response.RemoteGenreResponse
import com.example.data.model.response.RemoteMovieResponse
import com.example.data.model.response.RemoteReviewsResponse
import com.example.data.model.response.RemoteVideosResponse
import com.example.domain.model.Movie
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieService {

    @GET(value = "genre/movie/list")
    fun getGenreList(): Single<RemoteGenreResponse>

    @GET("discover/movie")
    fun getMoviesByGenre(
        @Query("page") page: Int,
        @Query("with_genres") with_genres: Int
    ): Single<RemoteMovieResponse>

    @GET("movie/{movie_id}")
    fun getMovieById(@Path("movie_id") id: Int): Single<Movie>

    @GET("movie/{movie_id}/reviews")
    fun getReviewByMovieId(
        @Path("movie_id") id: Int,
        @Query("page") page: Int
    ): Single<RemoteReviewsResponse>

    @GET("movie/{movie_id}/videos")
    fun getVideoByMovieId(@Path("movie_id") id: Int): Single<RemoteVideosResponse>

}