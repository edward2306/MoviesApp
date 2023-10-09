package com.example.movieapp.ui.detail.viewmodel

import androidx.lifecycle.ViewModel
import com.example.domain.model.Movie
import com.example.domain.model.response.ReviewResponse
import com.example.domain.model.response.VideoResponse
import com.example.domain.usecase.MovieReviewUseCase
import com.example.domain.usecase.MovieUseCase
import com.example.domain.usecase.MovieVideoUseCase
import com.example.movieapp.data.Resource
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class DetailViewModel(
    private val getMovieUseCase: MovieUseCase,
    private val getReviewUseCase: MovieReviewUseCase,
    private val getVideoUseCase: MovieVideoUseCase
) : ViewModel() {

    private val movieStateFlow = MutableStateFlow<Resource<Movie>>(Resource.empty())
    private val reviewStateFlow = MutableStateFlow<Resource<ReviewResponse>>(Resource.empty())
    private val videoStateFlow = MutableStateFlow<Resource<VideoResponse>>(Resource.empty())
    var disposable: Disposable? = null

    val movieState: StateFlow<Resource<Movie>>
        get() = movieStateFlow

    val reviewState: StateFlow<Resource<ReviewResponse>>
        get() = reviewStateFlow

    val videoState: StateFlow<Resource<VideoResponse>>
        get() = videoStateFlow

    fun fetchMovie(id: Int) {
        movieStateFlow.value = Resource.loading()

        disposable = getMovieUseCase.execute(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ res ->
                movieStateFlow.value = Resource.success(res)
            }, { throwable ->
                throwable.localizedMessage?.let {
                    movieStateFlow.value = Resource.error(it)
                }
            })
    }

    fun fetchReviews(id: Int, page: Int) {
        reviewStateFlow.value = Resource.loading()
        disposable = getReviewUseCase.execute(id, page)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : DisposableSingleObserver<ReviewResponse>() {
                override fun onSuccess(review: ReviewResponse) {
                    reviewStateFlow.value = Resource.success(review)
                }

                override fun onError(review: Throwable) {
                    reviewStateFlow.value = review.message?.let { Resource.error(it) }!!
                }
            })
    }

    fun fetchVideo(id: Int) {
        videoStateFlow.value = Resource.loading()
        disposable = getVideoUseCase.execute(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : DisposableSingleObserver<VideoResponse>() {
                override fun onSuccess(video: VideoResponse) {
                    videoStateFlow.value = Resource.success(video)
                }

                override fun onError(video: Throwable) {
                    videoStateFlow.value = video.message?.let { Resource.error(it) }!!
                }
            })
    }

}