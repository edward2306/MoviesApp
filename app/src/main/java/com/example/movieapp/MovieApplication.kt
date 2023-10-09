package com.example.movieapp

import android.app.Application
import com.example.movieapp.di.*
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class MovieApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@MovieApplication)
            modules(
                mainModule,
                genreModule,
                movieGenreModule,
                movieReviewModule,
                movieModule,
                movieVideoModule
            )
        }
    }
}