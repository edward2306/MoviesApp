package com.example.movieapp.util

import androidx.fragment.app.Fragment
import androidx.navigation.NavDirections
import androidx.navigation.fragment.NavHostFragment

fun Fragment.navigate(navDirections: NavDirections) {
    NavHostFragment.findNavController(this).navigate(navDirections)
}