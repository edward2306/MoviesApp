package com.example.movieapp.ui.genre.viewmodel

import androidx.lifecycle.ViewModel
import com.example.domain.model.response.GenreResponse
import com.example.domain.usecase.GenreListUseCase
import com.example.movieapp.data.Resource
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

class GenreViewModel(
    private val getGenreListUseCase: GenreListUseCase
) : ViewModel() {

    private val genreStateFlow = MutableStateFlow<Resource<GenreResponse>>(Resource.empty())
    var disposable: Disposable? = null

    private val searchQueryFlow = MutableSharedFlow<String>()

    val searchState: SharedFlow<String>
        get() = searchQueryFlow

    val genreState: StateFlow<Resource<GenreResponse>>
        get() = genreStateFlow

    fun fetchGenre() {
        genreStateFlow.value = Resource.loading()
        disposable = getGenreListUseCase.execute()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : DisposableSingleObserver<GenreResponse>() {
                override fun onSuccess(genre: GenreResponse) {
                    genreStateFlow.value = Resource.success(genre)
                }

                override fun onError(genre: Throwable) {
                    genreStateFlow.value = genre.message?.let { Resource.error(it) }!!
                }
            })
    }

}