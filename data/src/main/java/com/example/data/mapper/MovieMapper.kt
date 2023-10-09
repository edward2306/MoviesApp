package com.example.data.mapper

import com.example.data.model.response.RemoteGenreResponse
import com.example.data.model.response.RemoteMovieResponse
import com.example.data.model.response.RemoteReviewsResponse
import com.example.data.model.response.RemoteVideosResponse
import com.example.data.util.orDefault
import com.example.data.util.orFalse
import com.example.domain.model.Genre
import com.example.domain.model.response.MovieResponse
import com.example.domain.model.Movie
import com.example.domain.model.Review
import com.example.domain.model.Video
import com.example.domain.model.response.GenreResponse
import com.example.domain.model.response.ReviewResponse
import com.example.domain.model.response.VideoResponse

class MovieMapper {

    fun mapGenreListFromRemote(remoteGenreFromResponse: RemoteGenreResponse): GenreResponse {
        return GenreResponse(remoteGenreFromResponse.genres?.map { remoteReview ->
                Genre(
                    remoteReview.id.orDefault(),
                    remoteReview.name.orEmpty()
                )
            })
    }

    fun mapMovieGenreFromRemote(remoteMovieGenreFromResponse: RemoteMovieResponse): MovieResponse? {
        return remoteMovieGenreFromResponse.results?.map { remoteMovieGenre ->
            Movie(
                remoteMovieGenre.popularity.orDefault(),
                remoteMovieGenre.vote_count.orDefault(),
                remoteMovieGenre.video.orFalse(),
                remoteMovieGenre.poster_path.orEmpty(),
                remoteMovieGenre.id,
                remoteMovieGenre.adult.orFalse(),
                remoteMovieGenre.backdrop_path.orEmpty(),
                remoteMovieGenre.original_language.orEmpty(),
                remoteMovieGenre.original_title.orEmpty(),
                remoteMovieGenre.title,
                remoteMovieGenre.vote_average.orDefault(),
                remoteMovieGenre.overview.orEmpty(),
                remoteMovieGenre.release_date.orEmpty()
            )
        }?.let {
            MovieResponse(remoteMovieGenreFromResponse.page, remoteMovieGenreFromResponse.total_results,
                remoteMovieGenreFromResponse.total_pages, it
            )
        }
    }

    fun mapMovieByIdFromRemote(remoteMoviesResponse: Movie): Movie {
        return remoteMoviesResponse.let {remoteMovieGenre ->
            Movie(
                remoteMovieGenre.popularity.orDefault(),
                remoteMovieGenre.vote_count.orDefault(),
                remoteMovieGenre.video.orFalse(),
                remoteMovieGenre.poster_path.orEmpty(),
                remoteMovieGenre.id,
                remoteMovieGenre.adult.orFalse(),
                remoteMovieGenre.backdrop_path.orEmpty(),
                remoteMovieGenre.original_language.orEmpty(),
                remoteMovieGenre.original_title.orEmpty(),
                remoteMovieGenre.title,
                remoteMovieGenre.vote_average.orDefault(),
                remoteMovieGenre.overview.orEmpty(),
                remoteMovieGenre.release_date.orEmpty()
            )
        }
    }

    fun mapReviewMovieFromRemote(remoteReviewFromResponse: RemoteReviewsResponse): ReviewResponse {
        return ReviewResponse(remoteReviewFromResponse.page, remoteReviewFromResponse.total_results,
            remoteReviewFromResponse.total_pages, remoteReviewFromResponse.results?.map { remoteReview ->
                Review(
                    remoteReview.id.orEmpty(),
                    remoteReview.author.orEmpty(),
                    remoteReview.url.orEmpty(),
                    remoteReview.content.orEmpty()
                )
            })
    }

    fun mapVideoMovieFromRemote(remoteVideoFromResponse: RemoteVideosResponse): VideoResponse? {
        return remoteVideoFromResponse.id?.let {
            VideoResponse(
                it,
                remoteVideoFromResponse.results?.map {remoteVideo ->
                Video(
                    remoteVideo.id.orEmpty(),
                    remoteVideo.name.orEmpty(),
                    remoteVideo.key.orEmpty(),
                    remoteVideo.size.orDefault()
                )
            })
        }
    }
}