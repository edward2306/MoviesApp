package com.example.movieapp.ui.movie.viewmodel

import androidx.lifecycle.ViewModel
import com.example.domain.model.response.MovieResponse
import com.example.domain.usecase.MovieGenreUseCase
import com.example.movieapp.data.Resource
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class MovieViewModel(
    private val getMovieUseCase: MovieGenreUseCase,
) : ViewModel() {

    private var page = 2
    private val movieStateFlow = MutableStateFlow<Resource<MovieResponse>>(Resource.empty())
    private val movieStateFlowLoadMore = MutableStateFlow<Resource<MovieResponse>>(Resource.empty())
    var disposable: Disposable? = null

    val movieState: StateFlow<Resource<MovieResponse>>
        get() = movieStateFlow
    val movieStateLoadMore: StateFlow<Resource<MovieResponse>>
        get() = movieStateFlowLoadMore

    fun fetchMovie(id: Int) {
        movieStateFlow.value = Resource.loading()
        disposable = getMovieUseCase.execute(id, 1)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : DisposableSingleObserver<MovieResponse>() {
                override fun onSuccess(movies: MovieResponse) {
                    movieStateFlow.value = Resource.success(movies)
                }

                override fun onError(movies: Throwable) {
                    movieStateFlow.value = movies.message?.let { Resource.error(it) }!!
                }
            })
        page = 1
    }

    fun loadMoreMovie(id: Int) {
        movieStateFlowLoadMore.value = Resource.loading()
        disposable = getMovieUseCase.execute(id, page)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : DisposableSingleObserver<MovieResponse>() {
                override fun onSuccess(movies: MovieResponse) {
                    movieStateFlowLoadMore.value = Resource.loading()
                    movieStateFlowLoadMore.value = Resource.success(movies)
                }

                override fun onError(movies: Throwable) {
                    movieStateFlowLoadMore.value = movies.message?.let { Resource.error(it) }!!
                }
            })
        page++
    }

}