package com.example.movieapp.di

import androidx.recyclerview.widget.RecyclerView
import com.example.data.mapper.MovieMapper
import com.example.data.repository.MovieRepositoryImpl
import com.example.domain.repository.MovieRepository
import com.example.domain.usecase.*
import com.example.movieapp.ui.detail.adapter.ReviewAdapter
import com.example.movieapp.ui.detail.adapter.VideoAdapter
import com.example.movieapp.ui.detail.viewmodel.DetailViewModel
import com.example.movieapp.ui.genre.adapter.GenreAdapter
import com.example.movieapp.ui.genre.viewmodel.GenreViewModel
import com.example.movieapp.ui.movie.adapter.MovieAdapter
import com.example.movieapp.ui.movie.viewmodel.MovieViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val mainModule = module {
    single { MovieMapper() }
    factory<MovieRepository> { MovieRepositoryImpl(get()) }
}

val genreModule = module {
    factory { GenreListUseCase(get()) }
    viewModel { GenreViewModel(get()) }
    factory { GenreAdapter() }
}

val movieGenreModule = module {
    factory { MovieGenreUseCase(get()) }
    viewModel { MovieViewModel(get()) }
    factory { MovieAdapter() }
}

val movieReviewModule = module {
    factory { MovieReviewUseCase(get()) }
    viewModel { DetailViewModel(get(), get(), get()) }
    factory { ReviewAdapter() }
}

val movieModule = module {
    factory { MovieUseCase(get()) }
    viewModel { DetailViewModel(get(), get(), get()) }
}

val movieVideoModule = module {
    factory { MovieVideoUseCase(get()) }
    viewModel { DetailViewModel(get(), get(), get()) }
    factory { VideoAdapter() }
}